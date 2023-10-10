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
SynthDef('flute', {|
    freq = 440, vol = 1.0, pan = 0, attack = 0.15, sustain = 1.0, release = 0.1,
	gate = 1.0, done = 2, out = 0, modf = 440, shFreq = 4 |

    var sound, env, snd1, fltr, fmod, sandh, nFreq;

	env = Linen.kr(attackTime: attack, susLevel: sustain, releaseTime: release,
		           gate: gate, doneAction: done);

	// fmod = SinOsc.ar(freq: modf) * 0.7;
	// snd1 = Pulse.ar(freq: freq + LFNoise0.kr(freq: 0.3, mul: 100));
	snd1 = Pulse.ar(freq: freq);

	nFreq = LFNoise0.ar(freq: shFreq, mul: freq, add: freq * 2);

    //snd1 = RLPF.ar( in: snd1, freq: Lag.ar(in: LFNoise0.ar(freq: shFreq, mul: freq/2, add: freq) , lagTime: (1/9)/7 ), rq: 0.4) * 0.5;
	snd1 = RLPF.ar( in: RLPF.ar(in: snd1, freq: nFreq, rq: 0.5) , freq: nFreq, rq:0.5) * 0.5;

	// sound = RLPF.ar(in: (Saw.ar(freq: freq) * 0.4),
	// 	freq: ((env * 60) + freq + (SinOsc.ar(3.5) * 40)),
	// rq: 0.7);

    sound = Pan2.ar((snd1 * env * vol), pan);

	OffsetOut.ar(out, sound);
}).add;
)

a=Synth("flute", [\freq, 220, \modf, 30])

a.set("modf", 40)
a.set("shFreq", 9)
