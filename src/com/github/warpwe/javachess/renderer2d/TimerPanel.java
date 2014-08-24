/*
 * TimerPanel - A component to display a timer. Copyright (C) 2003 The Java-Chess team
 * <info@java-chess.de> This program is free software; you can redistribute it and/or modify it
 * under the terms of the GNU General Public License as published by the Free Software Foundation;
 * either version 2 of the License, or (at your option) any later version. This program is
 * distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the
 * implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General
 * Public License for more details. You should have received a copy of the GNU General Public
 * License along with this program; if not, write to the Free Software Foundation, Inc., 59 Temple
 * Place - Suite 330, Boston, MA 02111-1307, USA.
 */

package com.github.warpwe.javachess.renderer2d;

import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Iterator;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

/**
 * This class implements the functionality to display a timer
 */
public class TimerPanel extends JPanel implements Runnable {

  // Static variables

  /**
   * The generated ID.
   */
  private static final long serialVersionUID = -6041955605741561646L;

  /**
   * The width of the display in pixels.
   */
  static final int displayWidth = 70;

  /**
   * The height if of the display in pixels.
   */
  static final int displayHeight = 16;

  // Instance variables

  /**
   * The action listeners.
   */
  private ArrayList<ActionListener> actionListeners;

  /**
   * The timer thread.
   */
  private volatile Thread timerThread;

  /**
   * A label to display the time.
   */
  private JLabel display;

  /**
   * The time left in milliseconds.
   */
  private long time;

  /**
   * The remaining time until timeout.
   */
  long remainingTime;

  /**
   * The direction of the counting.
   */
  private boolean countdown;

  // Constructors
  /**
   * Create a new timer panel.
   *
   * @param countdown
   *          Flag to indicate, if we count downwards.
   */
  public TimerPanel(boolean countdown) {
    setCountingDirection(countdown);
    this.actionListeners = new ArrayList<ActionListener>();
    this.display = new JLabel("0:00:00", JLabel.RIGHT);
    this.display.setPreferredSize(new Dimension(displayWidth, displayHeight));
    this.display.setHorizontalTextPosition(SwingConstants.CENTER);
    add(display);
  }

  // Methods
  /**
   * Add a action listener to this timer.
   *
   * @param listener
   *          The action listener to add.
   */
  public void addActionListener(ActionListener listener) {
    actionListeners.add(listener);
  }

  /**
   * Notify the waiting action listeners.
   *
   * @param event
   *          The action event to send.
   */
  private void notifyListeners(ActionEvent event) {

    // Iterate over all the listeners and send them the event.
    for (Iterator<ActionListener> iterator = actionListeners.iterator(); iterator.hasNext();) {
      ActionListener listener = (ActionListener) iterator.next();
      listener.actionPerformed(event);
    }
  }

  /**
   * Set the length of the countdown in seconds.
   *
   * @param time
   *          The length of the countdown.
   */
  public void setCountdown(int time) {
    remainingTime = this.time = 1000L * (long) time;
    display(this.time);
  }

  /**
   * Start the timer.
   */
  public void start() {
    if (this.time > 0L) {
      if (timerThread == null) {
        timerThread = new Thread(this);
        timerThread.start();
      }
    }
  }

  /**
   * Stop the timer.
   */
  public void stop() {

    // If there is a running thread.
    if (timerThread != null) {
      Thread newTimerThread = timerThread;  // Copy it.
      timerThread = null;                // Signal to stop the thread.
      try {
        newTimerThread.join();             // And wait for the thread to end.
      }
      catch (InterruptedException ignored) {
      }
    }
  }

  /**
   * The actual thread method.
   */
  public void run() {

    long startTime = System.currentTimeMillis();  // The time when this run started.
    long runningTime;                             // The length of this run.
    try {
      // While there's still time left and noone stopped the thread.
      do {
        Thread.sleep(100);  // Wait 1/10 of a second.
        runningTime = System.currentTimeMillis() - startTime;
        display(remainingTime - runningTime);  // decrease the time by a second and display it.
      }
      while ((remainingTime >= runningTime) && (Thread.currentThread() == timerThread));

      remainingTime -= runningTime;  // Substract the length of this thinking time.

      // Now notify all the action listeners, that the timer has stopped.
      notifyListeners(new ActionEvent(this, ActionEvent.ACTION_PERFORMED,
          (remainingTime < 0L) ? "timeout" : "interrupted"));

    }
    catch (InterruptedException ignored) {
    }
  }

  /**
   * Display the current time.
   *
   * @param time
   *          The current time in milliseconds.
   */
  void display(long time) {

    // Don't display negative time.
    if (time < 0L) {
      time = 0L;
    }

    if (!isCountdown()) {
      time = this.time - time;
    }

    int timeSec = (int) (time / 1000L);  // Display accurate to 1 s.
    StringBuffer timeString = new StringBuffer();
    int hours = timeSec / 3600;
    timeString.append("" + hours);
    int minutes = (timeSec / 60) % 60;
    timeString.append(((minutes < 10) ? ":0" : ":") + minutes);
    int seconds = timeSec % 60;
    timeString.append(((seconds < 10) ? ":0" : ":") + seconds);
    display.setText(timeString.toString());
    display.paintImmediately(0, 0, displayWidth, displayHeight);
  }

  /**
   * Check, if this timer counts downwards.
   *
   * @return true, if the counter counts downwards.
   */
  private final boolean isCountdown() {
    return countdown;
  }

  /**
   * Set the counting direction.
   *
   * @param countdown
   *          Flag to indicate, if we want a countdown.
   */
  private final void setCountingDirection(boolean countdown) {
    this.countdown = countdown;
  }

  /**
   * Get the remaining time in seconds.
   *
   * @return The remaining time in seconds.
   */
  public int getRemainingTime() {
    return (int) (time / 1000L);
  }

  public void alignText() {
    this.display.setHorizontalTextPosition(SwingConstants.CENTER);
    this.display.setHorizontalAlignment(SwingConstants.CENTER);
  }
}
