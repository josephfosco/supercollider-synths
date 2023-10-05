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
SynthDef('bell-soft', {|
    freq = 440, vol = 1.0, pan = 0, attack = 0.03, decay = 0.03, sustain = 0.4, release = 0.4,
	land= 0.9, gate = 1.0, done = 2, out = 0|

    var sound, env;

	env = EnvGen.kr(Env.perc(attackTime: 0.065, releaseTime: 4.0));

	sound = ((SinOsc.ar(freq: freq)
                          ) *
                         env
                         );

    sound = Pan2.ar((sound * vol), pan);

	OffsetOut.ar(out, sound);
}).add;
)

a=Synth("bell-soft", [\freq, 880],)
