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



// from https://sccode.org/1-4QH


//****************************************
// Fig 57.3: Babbling R2D2 computer noises

// "[...] it might not even make any good noises for a while,
// but sometimes it makes great computer babble sounds". (Andy Farnell)

(
w = {   |period=0|
        var change, rate, sig, carrierFreq, cfRamp, carrierLvl, clRamp,
        modulatorRatio, mrRamp, modulatorIndex, miRamp, outputAmplitude, oaRamp;

        period = period * 600 + 100;

        // Calculation of a recursive working metronome (Impulse.kr) that generates its
        // changing frequency out of its own impulses.
        change = Impulse.kr(LocalIn.kr(1,10));
        rate = CoinGate.kr(1/3, change);
        rate = (TChoose.kr(rate, period/((0..1) + 1))/1000).reciprocal;
        LocalOut.kr(rate);

        # carrierFreq, cfRamp = TIRand.kr(0, [1000, 1], change);
        carrierFreq = Ramp.kr( carrierFreq / 1000, (cfRamp * period) / 1000 ) * 0.6;

        # carrierLvl, clRamp = TIRand.kr(0, [9000, 1], CoinGate.kr(1/3, change));
        carrierLvl = Ramp.kr( carrierLvl, (clRamp * period) / 1000) + 100;

        # modulatorRatio, mrRamp = TIRand.kr([800,1], CoinGate.kr(1/4, change));
        modulatorRatio = Ramp.kr(modulatorRatio, (mrRamp * period) / 1000) + 20;

        # modulatorIndex, miRamp = TIRand.kr(0, [100, 1], CoinGate.kr(1/4, change));
        modulatorIndex = Ramp.kr(modulatorIndex / 200, (miRamp * period) / 1000) + 0.2;

        # outputAmplitude, oaRamp = TIRand.kr(0!2, 1!2, CoinGate.kr(1/2, change));
        outputAmplitude = Ramp.kr(outputAmplitude, (oaRamp * period + 3) / 1000);

        // jointed FM Synthesizer
        sig = LFSaw.ar(carrierFreq, 1, 0.5, 0.5) * carrierLvl;
        sig = sig + SinOsc.ar(carrierFreq * modulatorRatio) * modulatorIndex;
        sig = cos(sig * 2pi) * outputAmplitude;

        // One pole filters:
        sig = OnePole.ar(sig, exp(-2pi * (10000 * SampleDur.ir)));
        sig = OnePole.ar(sig, exp(-2pi * (10000 * SampleDur.ir)));
        sig = (sig - OnePole.ar(sig, exp(-2pi * (100 * SampleDur.ir))));
        sig = (sig - OnePole.ar(sig, exp(-2pi * (100 * SampleDur.ir))));
        sig = sig!2 * 0.06;
}.play;
)

// period controls the talk-speed. range: 0-1. 0 matches to fast, 1 to slow:
w.set(\period, 1);
w.set(\period, 0);
w.set(\period, 0.5);
w.set(\period, 0.7);
w.set(\period, 0.3);
