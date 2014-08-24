/*
 * CountdownTimerPanel - A component to display a countdown timer. Copyright (C) 2003 The Java-Chess
 * team <info@java-chess.de> This program is free software; you can redistribute it and/or modify it
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
public class CountdownTimerPanel extends JPanel implements Runnable {

  // Static variables

  /**
   * The generated ID.
   */
  private static final long serialVersionUID = -4927888503079651421L;

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
  public volatile Thread timerThread;

  /**
   * A label to display the time.
   */
  JLabel display;

  /**
   * The time left in milliseconds.
   */
  long time;

  /**
   * The remaining time until timeout.
   */
  long remainingTime;

  /**
   * The start time of the thread.
   */
  // long startTime;

  // Constructors
  /**
   * Create a new countdown timer panel.
   */
  public CountdownTimerPanel() {
    actionListeners = new ArrayList<ActionListener>();
    // _startTime = -1L;
    display = new JLabel("0:00:00", JLabel.RIGHT);
    display.setPreferredSize(new Dimension(displayWidth, displayHeight));
    display.setHorizontalTextPosition(SwingConstants.CENTER);
    add(display);
  }

  // Methods

  /**
   * Add a action listener to this timer.
   *
   * @param listener
   *          The action listener to add.
   */
  public final void addActionListener(final ActionListener listener) {
    actionListeners.add(listener);
  }

  /**
   * Notify the waiting action listeners.
   *
   * @param event
   *          The action event to send.
   */
  private void notifyListeners(final ActionEvent event) {

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
  public final void setCountdown(final int time) {
    // _startTime = -1L;
    remainingTime = this.time = 1000L * (long) time;
    // FIXME SE Propably the wrong time is displayed.
    // display(_time);
    display(remainingTime);
  }

  /**
   * Start the timer.
   */
  public final void start() {
    if (time > 0L) {
      // if( _startTime == -1L) {
      // _startTime = System.currentTimeMillis();
      // }
      if (this.timerThread == null) {
        this.timerThread = new Thread(this);
        this.timerThread.start();
      }
    }
  }

  /**
   * Stop the timer.
   */
  public final void stop() {

    // If there is a running thread.
    if (this.timerThread != null) {
      // Copy it.
      Thread newTimerThread = this.timerThread;
      // Signal to stop the thread.
      this.timerThread = null;
      try {
        // And wait for the thread to end.
        newTimerThread.join();
      }
      catch (InterruptedException ignored) {
      }
    }
  }

  /**
   * The actual thread method.
   */
  public final void run() {

    long startTime = System.currentTimeMillis();  // The time when this run started.
    long runningTime;                             // The length of this run.
    try {
      // While there's still time left and noone stopped the thread.
      do {
        Thread.sleep(100);  // Wait 1/10 of a second.
        runningTime = System.currentTimeMillis() - startTime;
        display(this.remainingTime - runningTime);  // decrease the time by a second and display it.
      }
      while ((this.remainingTime >= runningTime) && (Thread.currentThread() == this.timerThread));

      this.remainingTime -= runningTime;  // Substract the length of this thinking time.

      // Now notify all the action listeners, that the timer has stopped.
      notifyListeners(new ActionEvent(this, ActionEvent.ACTION_PERFORMED,
          (this.remainingTime < 0L) ? "timeout" : "interrupted"));

    }
    catch (InterruptedException ignored) {
    }
  }

  /**
   * Display the current time.
   *
   * @param displayedTime
   *          The current time in milliseconds.
   */
  public void display(long displayedTime) {

    // Don't display negative time.
    if (displayedTime < 0) {
      displayedTime = 0;
    }

    int timeSec = (int) (displayedTime / 1000L);  // Display accurate to 1 s.
    StringBuffer timeString = new StringBuffer();
    int hours = timeSec / 3600;
    timeString.append("" + hours);
    int minutes = (timeSec / 60) % 60;
    timeString.append(((minutes < 10) ? ":0" : ":") + minutes);
    int seconds = timeSec % 60;
    timeString.append(((seconds < 10) ? ":0" : ":") + seconds);
    this.display.setText(timeString.toString());
    this.display.paintImmediately(0, 0, displayWidth, displayHeight);
  }

  /**
   * Get the remaining time in seconds.
   *
   * @return The remaining time in seconds.
   */
  public int getRemainingTime() {
    return (int) (time / 1000L);
  }

  public final void alignText() {
    this.display.setHorizontalTextPosition(SwingConstants.CENTER);
    this.display.setHorizontalAlignment(SwingConstants.CENTER);
  }
}
