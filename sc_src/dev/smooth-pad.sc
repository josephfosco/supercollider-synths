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
SynthDef('smooth-pad', {|
    freq = 440, vol = 0.5, pan = 0, attack = 0.5, sustain = 1.0, release = 2.0,
	gate = 1.0, done = 2, out = 0 |

    var sound, env, snd1, fltr, fmod, sandh, nFreq, fx, n=6, final, phaser,
	delay1, delay2, modulationRate, modDepth, modulatedDelay1, modulatedDelay2, output;


	env = Linen.kr(attackTime: attack, susLevel: sustain, releaseTime: release,
		           gate: gate, doneAction: done);

	snd1 = Mix.new([ SinOsc.ar(freq: freq, mul: 0.25),
		SinOsc.ar(freq: (freq * 2), mul: 0.12),
		SinOsc.ar(freq: freq + (freq * LFNoise1.ar(freq: 5, mul: 0.01)), mul: 0.25)
	]);

	// fx= Mix.fill(n, {
	// 	var modulationRate = rrand(0.1, 0.5); // LFO rate (in Hz)
	// 	var modDepth = 0.2; // Modulation depth (in seconds)
	// 	var modulatedDelay1 = SinOsc.ar(modulationRate).range(modDepth * -1, modDepth);
	// 	var maxdelaytime= rrand(0.01,0.25);
	// 	DelayC.ar(snd1,
	// 	delaytime: maxdelaytime, add: modulatedDelay1);
	// });


    output = snd1 * vol; // Adjust the overall output level

    // Apply feedback for a smoother chorus effect
	// output = CombL.ar(output, maxdelaytime: 0.003, delaytime: SinOsc.ar(freq: 0.2, mul: 0.001, add: 0.0015), decaytime: 0.005); // Apply low-pass comb filter for feedback


    sound = Pan2.ar((output * env), pan);
	// phaser = AllpassN.ar(sound,0.02,SinOsc.kr(freq,0,0.01,0.01)); //max delay of 20msec

	// final = Mix.ar([fx, phaser]);

	OffsetOut.ar(out, sound);
}).add;
)

a=Synth("smooth-pad", [\freq, 440, \modf, 30])
