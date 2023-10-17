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
SynthDef(\seqSynth,
{
var a, b, t = Impulse.kr(2), freq1, snd;
a = { Dseq([1, 2, 3, 4, 5], inf) } * 110;
freq1=Demand.kr(t, 0, [a]);
snd=SinOsc.ar(freq: freq1);
Out.ar(0, snd);

}).add
)

a=Synth(\seqSynth)


(
SynthDef(\seqSynth, {
	arg freqs=[1.0, 2.0];
var a, b, t = Impulse.kr(2), freq1, snd;
a = { Dseq(freqs, inf) } * 110;
freq1=Demand.kr(t, 0, [a]);
snd=SinOsc.ar(freq: freq1);
Out.ar(0, snd);

}).add
)

a=Synth(\seqSynth)
