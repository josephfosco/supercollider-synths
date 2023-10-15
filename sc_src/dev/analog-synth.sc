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
SynthDef.new('anasynth', {
		arg freq=440, vol=0.5, pan=0.0, amp=0.3, cutoff=1.7, gain=3.0, gate=1, out=0;
    var signal, filter, env;
    signal = Saw.ar(freq, amp);
    //env = EnvGen.ar(Env.adsr(0.2, 0.2, 0.8, 1), gate: gate, doneAction:2);
    env = EnvGen.ar(Env.perc(0.03, 0.4), gate: gate, doneAction:2);
	filter = MoogFF.ar(signal * env, ((freq * cutoff) - (freq/4)) + (env * freq/4), gain );
    Out.ar(out, Pan2.ar(filter, pan));	}
).add;
)

a=Synth("anasynth", [\freq, 220, \vol, 0.15],)

plotTree(s)


(
  Pbind(
	\instrument, \anasynth,
	\freq, Prand([440, 493.88, 554.37, 587.33, 659.25, 739.99, 830.61, 880], inf),
	\dur, 0.25
  ).play
)

(
  Pbind(
	\instrument, \anasynth,
	\freq, Pfunc({rrand(100, 2000)}),
	\dur, 0.25
  ).play
)