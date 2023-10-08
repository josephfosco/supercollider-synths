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

// Modified from: https://sccode.org/1-5cP «simple offset patterns v01» by neill.vermaak

(
SynthDef.new('bell-soft', {
		arg freq=440, vol=0.5, pan=0.0, dur=1.0, lfor1=0.08, filt=5000;
		var sig, lfo1, env;

		lfo1  = SinOsc.kr(lfor1, 0.5, 1, 0);
		sig   = SinOscFB.ar(freq, lfo1, 1, 0);
		env   = Line.kr(1, 0, dur*4.0, doneAction: Done.freeSelf);
		sig   = MoogFF.ar(sig, filt * 0.5, 0, 0, 1, 0);
		sig   = Pan2.ar(sig, pan, vol);
	    sig   = FreeVerb2.ar(sig[0], sig[1], 0.5, 0.99, 0.9);
		sig   = (sig) * env;
		Out.ar(0, sig * 0.6);
	}
).add;
)

a=Synth("bell-soft", [\freq, 880, \dur, 1.0, \vol, 0.15],)

plotTree(s)

// .writeDefFile("/home/joseph/src/clj/splice/src/splice/instr/instruments/sc/");
