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
        freq=110, vol=0.5, pan=0|
        var snd;

        // the drone zone!
	snd = Mix.ar(
            VarSaw.ar(
                freq: Lag.kr(freq * SinOsc.kr(LFNoise0.kr(1)).range(0.99,1.01),1),
                width: SinOsc.kr(LFNoise0.kr(1)).range(0.4,0.6),
                mul: vol ,
            ) +
            VarSaw.ar(
                freq: Lag.kr(2*freq * SinOsc.kr(LFNoise0.kr(1)).range(0.99,1.01),1),
                width: SinOsc.kr(LFNoise0.kr(1)).range(0.4,0.6),
                mul: vol/2,
            ) +
            VarSaw.ar(
                freq: Lag.kr(3*freq * SinOsc.kr(LFNoise.kr(1)).range(0.99,1.01),1),
                width: SinOsc.kr(LFNoise0.kr(1)).range(0.4,0.6),
                mul: vol/3,
            )
        );

	snd = RLPF.ar(in: snd, freq: LFNoise1.kr(100).range(freq*2, freq*3), rq: 1.0);

        // spread the signal
	    snd = Splay.ar(snd);

        // pan
        snd = Balance2.ar(snd[0] ,snd[1],SinOsc.kr(
            LFNoise0.kr(0.1).range(0.05,0.2)
        )*0.1);

        // make sound!
        Out.ar(0,snd);
    }
).add;
)

a=Synth("drone01")


a=Synth("drone01")

plotTree(s)

// .writeDefFile("/home/joseph/src/clj/splice/src/splice/instr/instruments/sc/");


// play({ PMOsc.ar(110, 115, Line.ar(0,20,8), 0, 0.1) }); // modulate index

// (
// SynthDef('drone01', {|
//     freq = 164.81, vol = 1, pan = 0, clickiness = 0.1, out = 0|

//     var snd, click;

//     // Basic tone is a SinOsc
//     snd = SinOsc.ar(freq) * EnvGen.ar(Env.perc(0.03, Rand(3.0, 4.0), 1, -7), doneAction: 2);
//     snd = HPF.ar( LPF.ar(snd, 380), 120);
//     // The "clicking" sounds are modeled with a bank of resonators excited by enveloped white noise
//     click = DynKlank.ar(`[
//         // the resonant frequencies are randomized a little to add variation
//         // there are two high resonant freqs and one quiet "bass" freq to give it some depth
//         [240*ExpRand(0.97, 1.02), 2020*ExpRand(0.97, 1.02), 3151*ExpRand(0.97, 1.02)],
//         [-9, 0, -5].dbamp,
//         [0.8, 0.07, 0.08]
//     ], BPF.ar(PinkNoise.ar, 6500, 0.1) * EnvGen.ar(Env.perc(0.001, 0.01))) * clickiness;
//     snd = (snd*clickiness) + (click*(1-clickiness));

//     snd = Pan2.ar(snd, pan);

//     OffsetOut.ar(out, snd * vol);
// }).add;
// )
