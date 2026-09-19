import { WAKE } from './voice'

/**
 * His name, heard at the ignition screen.
 *
 * The clap listener already proves the principle: the button should not be the
 * only way in. But a clap is a blunt instrument — it says "something happened",
 * not "someone wants you" — and the first thing anybody tries in front of a
 * dark reactor is to say his name. Before this, that did nothing, because the
 * real voice loop is started inside `ignite()` and so does not exist until
 * after the very gesture it would be replacing.
 *
 * So this is a deliberately small second recogniser with one job: match the
 * wake word, call back once, and get out of the way. It shares the WAKE pattern
 * with the main loop — including the mishearings Chrome insists on (Travis,
 * Jervis, Java's) — so a name that wakes him at the ignition screen is exactly
 * the set of names that wakes him afterwards.
 *
 * It must be torn down before the real loop starts. Two recognisers on one
 * microphone is how you get an assistant that hears half of what you say, which
 * is the same reason `listenForClap` is unwound the moment he boots.
 *
 * Silent about failure, for the same reason the clap listener is: this is an
 * alternative to a button that is still right there on screen. A browser with
 * no SpeechRecognition (Safari, Firefox) or a refused microphone simply gets
 * the button, with nothing to read about a feature they did not ask for.
 */
export type WakeListener = { stop: () => void }

/** One utterance produces several partials carrying his name. */
const DEBOUNCE_MS = 2000

export function listenForWakeWord(onWake: () => void): WakeListener {
  const Ctor =
    (window as any).SpeechRecognition ?? (window as any).webkitSpeechRecognition
  if (!Ctor) return { stop: () => {} }

  let stopped = false
  let rec: any = null
  let last = 0

  const fire = (text: string) => {
    const now = Date.now()
    if (now - last < DEBOUNCE_MS) return
    if (!WAKE.test(text)) return
    last = now
    onWake()
  }

  /**
   * Chrome ends a recognition session on its own — on silence, on a result, on
   * a throttle it does not announce — and a listener that does not restart is a
   * wake word that works once and then quietly never again. Restarting from
   * `onend` is what makes this survive a minute of someone staring at the
   * screen deciding what to say.
   */
  const start = () => {
    if (stopped) return
    try {
      rec = new Ctor()
    } catch {
      return
    }
    rec.continuous = true
    rec.interimResults = true
    rec.lang = 'en-US'

    rec.onresult = (e: any) => {
      for (let i = e.resultIndex; i < e.results.length; i++) {
        fire(String(e.results[i][0]?.transcript ?? ''))
      }
    }
    // A refused microphone lands here. Staying quiet is the whole contract.
    rec.onerror = () => {}
    rec.onend = () => {
      if (!stopped) setTimeout(start, 300)
    }

    try {
      rec.start()
    } catch {
      /* already running, or refused — the button still works */
    }
  }

  start()

  return {
    stop: () => {
      stopped = true
      try {
        rec?.abort()
      } catch {
        /* nothing to abort */
      }
      rec = null
    },
  }
}
