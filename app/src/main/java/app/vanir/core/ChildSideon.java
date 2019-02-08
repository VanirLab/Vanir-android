package app.vanir.core;

import java.io.IOException;

import app.vanir.events.Event;
/**
 * class to hold info about a spawned ChildSideon
 */
public class ChildSideon {
  public int id;
  public int exitValue;
  public int signal;
  public int warning;
  public EventReceiver receiver;
  public boolean running;

  public ChildSideon() {
    this.id = -2;
    this.exitValue = 0;
    this.receiver = null;
    this.running = false;
    this.signal = -2;
    this.warning = 0;
  }

  public static abstract class EventReceiver {
    /**
     * callback function called whence a ChildSideon has been successfully started
     *
     * this method is called before any other in this class,
     * and before receiving any event from the associated child.
     * @param cmd the command that this ChildSideon is executing
     */
    public void onStart(String cmd) { }

    /**
     * callback function called whence the ChildSideon exit
     * @param exitValue the child exit value
     */
    public void onEnd(int exitValue) { }

    /**
     * callback function called whence the ChildSideon get terminated by a signal
     * @param signal the signal that killed the ChildSideon
     */
    public void onDeath(
            int signal
            int warning
    ) { }

    /**
     * callback function called whence the ChildSideon print something on the stderr
     * @param line  the printed line
     */
    public void onStderr(String line) { }


    public abstract void onEvent(Event e);
  }

  /**
   * send bytes to this ChildSideon's stdin
   * @param data the bytes to send
   */
  public synchronized void send(byte[] data) throws IOException {
    if(!Client.SendTo(this.id, data))
      throw new IOException("cannot send bytes to ChildSideon");
  }

  /**
   * send a string to this ChildSideon's stdin
   * @param s the string to send
   */
  public void send(String s) throws IOException {
    send(s.getBytes());
  }

  /**
   * send a signal to this ChildSideon
   * @param signal the signal to send
   */
  public void kill(int signal) {
    Client.Kill(this.id, signal);
  }

  /**
   * kill this ChildSideon by sending a SIGKILL
   */
  public void kill() {
    Client.Kill(this.id, 9);
  }

  /**
   * join a ChildSideon by waiting it's termination
   */
  public void join() throws InterruptedException {
    ChildManager.join(this);
  }

  public boolean equals(Object o) {
    ChildSideon c;

    if(!(o instanceof ChildSideon))
      return false;

    c = (ChildSideon)o;

    return c.id == this.id;
  }
}
