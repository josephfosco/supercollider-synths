//    Copyright (C) 2023  Joseph Fosco. All Rights Reserved
//
//    This program is free software: you can redistribute it and/or modify
//    it under the terms of the GNU General Public License as published by
//    the Free Software Foundation, either version 3 of the License, or
//    (at your option) any later version.
//
//    This program is distributed in the hope that it will be useful,
//    but WITHOUT ANY WARRANTY; without even the implied warranty of
//    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
//    GNU General Public License for more details.
//
//    You should have received a copy of the GNU General Public License
//    along with this program.  If not, see <http://www.gnu.org/licenses/>.

(
SynthDef(\customSequencer, {
    arg freq = 440, seq = [0, 2, 4, 5, 7, 9, 11]; // Default sequence is a major scale

    var trigger, env, note, sound;

    // Create a Pbind pattern for sequencing notes and durations
	var pattern = Pbind(
		\freq, Pfunc { |ev| Pseq(seq, inf).asStream.next },
		\dur, Pseq([0.2], inf)  // Set the duration for the notes (adjust as needed)
	).asStream;

    // Create a trigger to advance to the next note
    trigger = Impulse.kr(0);

    // Sample-and-hold the trigger signal to get discrete triggers
    trigger = T2A.ar(trigger);

    // Get the next note from the pattern when the trigger fires
    note = pattern.next(\freq);
	// note = 7;

    // Synthesize sound using the note parameter
    // var sound = SinOsc.ar(freq + note, 0, 0.2);
    sound = SinOsc.ar(freq + note, 0, 0.2);

    // Envelope the sound with a short envelope
    env = EnvGen.kr(Env.perc(0.01, 0.2), doneAction: 2);
    sound = sound * env;

    // Output the sound
    Out.ar(0, sound);
}).add;
)

// Example usage: create a custom sequencer synth and play it
x = Synth(\customSequencer, [\freq, 440, \seq, [0, 2, 3, 5, 7]]);
x.set(\trigger, 1);  // Trigger the sequencer to move to the next note






(
SynthDef(\customSequencer, {
    arg freq = 440, seq = [0, 2, 4, 5, 7, 9, 11]; // Default sequence is a major scale

    var trigger, env, note, sound, pattern;

    // Create a Pbind pattern for sequencing notes and durations
    pattern = Pseq(seq, inf);

    // Create a trigger to advance to the next note
    trigger = Impulse.kr(0);

    // Sample-and-hold the trigger signal to get discrete triggers
    trigger = T2A.ar(trigger);

    // Get the next note from the pattern when the trigger fires
    // note = pattern.next(\freq);
    // Get the next note from the pattern when the trigger fires
    note = Demand.kr(trigger, 0, pattern);

    // Synthesize sound using the note parameter
    // var sound = SinOsc.ar(freq + note, 0, 0.2);
    sound = SinOsc.ar(freq + note.midicps, 0, 0.2);

    // Envelope the sound with a short envelope
    env = EnvGen.kr(Env.perc(0.01, 0.2), doneAction: 2);
    sound = sound * env;

    // Output the sound
    Out.ar(0, sound);
}).add;
)

// Example usage: create a custom sequencer synth and play it
x = Synth(\customSequencer, [\freq, 440, \seq, [0, 2, 3, 5, 7]]);
x.set(\trigger, 1);  // Trigger the sequencer to move to the next note
