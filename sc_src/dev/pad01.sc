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
SynthDef('pad01', {|
    freq = 440, vol = 1.0, pan = 0, attack = 0.15, sustain = 1.0, release = 0.1,
	gate = 1.0, done = 2, out = 0 |

    var sound, env, snd1, fltr, fmod, sandh, nFreq, fx, n=8, final, phaser ;

	env = Linen.kr(attackTime: attack, susLevel: sustain, releaseTime: release,
		           gate: gate, doneAction: done);

	snd1 = RLPF.ar(in: Saw.ar(freq: freq) + (Pulse.ar(freq: freq/2) * 0.3) * 0.5, freq: freq * 2);
	fx= Mix.fill(n, {
		var maxdelaytime= rrand(0.01,0.03);
		DelayC.ar(snd1, maxdelaytime, LFNoise1.kr(Rand(5,10),0.01,0.02) )
	});

    sound = Pan2.ar((fx * env * vol), pan);
	phaser = AllpassN.ar(sound,0.02,SinOsc.kr(freq,0,0.01,0.01)); //max delay of 20msec

	final = Mix.ar([fx, phaser]);


	 OffsetOut.ar(out, sound);
}).add;
)

a=Synth("pad01", [\freq, 440, \modf, 30])
