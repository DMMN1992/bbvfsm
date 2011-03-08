/*******************************************************************************
 *  Copyright 2010, 2011 bbv Software Services AG, Ueli Kurmann
 *
 *    Licensed under the Apache License, Version 2.0 (the "License");
 *    you may not use this file except in compliance with the License.
 *    You may obtain a copy of the License at
 *
 *        http://www.apache.org/licenses/LICENSE-2.0
 *
 *    Unless required by applicable law or agreed to in writing, software
 *    distributed under the License is distributed on an "AS IS" BASIS,
 *    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *    See the License for the specific language governing permissions and
 *    limitations under the License.
 *
 * Contributors:
 *     bbv Software Services AG (http://www.bbv.ch), Ueli Kurmann
 *******************************************************************************/
package ch.bbv.fsm.example;

/**
 * Elevator states.
 * 
 * @author Ueli Kurmann (bbv Software Services AG) (bbv Software Services AG)
 * 
 */
enum States {
    // / <summary>Elevator has an Error</summary>
    Error,

    // / <summary>Elevator is healthy, i.e. no error</summary>
    Healthy,

    // / <summary>The elevator is moving (either up or down)</summary>
    Moving,

    // / <summary>The elevator is moving down.</summary>
    MovingUp,

    // / <summary>The elevator is moving down.</summary>
    MovingDown,

    // / <summary>The elevator is standing on a floor.</summary>
    OnFloor,

    // / <summary>The door is closed while standing still.</summary>
    DoorClosed,

    // / <summary>The dor is open while standing still.</summary>
    DoorOpen
}