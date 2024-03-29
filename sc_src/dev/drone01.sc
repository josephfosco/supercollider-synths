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
SynthDef("drone01", {|
    freq=110, vol=0.9, pan=0, attack=4.0, sustain=0.75, release=4.0,
	gate = 1.0, done = 2, verbMix=0.6, verbRoom=1.0, verbDamp=0.9, out = 0 |

    var vol_adjust=0.5, envelope, env, sound, snd1, snd2, snd3, lpfFreq;

	// env = Linen.kr(attackTime: attack, susLevel: sustain, releaseTime: release,
	// gate: gate, doneAction: done);

	// create an envelope that allows time at the end for the reverb tail to fade
	envelope = Env.new(levels: [0, sustain, 0], times: [attack, release], curve: [\sin, \sin], releaseNode: 1);
	env = EnvGen.kr(envelope, gate: gate, doneAction: done);

    snd1 = VarSaw.ar(
		freq: Lag.kr(freq * SinOsc.kr(LFNoise0.kr(1)).range(0.99,1.01),1),
		width: SinOsc.kr(LFNoise0.kr(1)).range(0.4,0.6),
	) / 4;
    snd2 = VarSaw.ar(
		freq: Lag.kr(1.5*freq * SinOsc.kr(LFNoise0.kr(1)).range(0.99,1.01),1),
		width: SinOsc.kr(LFNoise0.kr(1)).range(0.4,0.6),
	) / 6;

	snd3 = VarSaw.ar(
		freq: Lag.kr(2*freq * SinOsc.kr(LFNoise0.kr(1)).range(0.99,1.01),1),
		width: SinOsc.kr(LFNoise0.kr(1)).range(0.4,0.6),
	) / 5;

	lpfFreq = Lag.ar(in: LFNoise0.ar(freq: 0.1).range(freq, freq * 5), lagTime: 7.0);
	snd1 = RLPF.ar(in: snd1, freq: lpfFreq, rq: 0.2, mul: vol_adjust);
	snd2 = RLPF.ar(in: snd2, freq: lpfFreq, rq: 0.2, mul: vol_adjust);
	snd3 = RLPF.ar(in: snd3, freq: lpfFreq, rq: 0.2, mul: vol_adjust);


	sound = Mix.ar(
	    [
	        Pan2.ar(snd1, Lag.ar(in: LFNoise0.ar(freq: 0.15, mul: 0.9), lagTime: 3.0)),
		    Pan2.ar(snd2, Lag.ar(in: LFNoise0.ar(freq: 0.17, mul: 0.7), lagTime: 3.0)),
		    Pan2.ar(snd3, Lag.ar(in: LFNoise0.ar(freq: 0.1, mul: 0.8), lagTime: 3.0))
	    ]
	) * env;

	// sound = FreeVerb2.ar(in: (sound[0] * vol), in2: (sound[1] * vol),
	// mix: verbMix, room: verbRoom, damp: verbDamp);

	sound.scope;

	OffsetOut.ar(out, sound);
    }
).add;
)

a=Synth("drone01")

a.set("gate", 0)

a.set('verbDamp', 1.0)
a.set('verb.Room', 1.0)
a.set('verbMox', 0.9)

// .writeDefFile("/home/joseph/src/clj/splice/src/splice/instr/instruments/sc/");
