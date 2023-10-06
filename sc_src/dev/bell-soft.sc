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
    freq = 440, vol = 0.7, pan = 0, attack = 0.015, decay = 0.3, release = 4.0,
	land= 0.9, gate = 1.0, done = 2, out = 0|

    var sound, env, bellEnv;

	bellEnv = Env.new(levels: [0, 0.8, 0.6, 0], times: [attack, decay, release]);
	env = EnvGen.kr(bellEnv);

	sound = (SinOsc.ar(freq: freq) +
		Pulse.ar(freq: freq, mul: 0.07))
	        * env;

    sound = Pan2.ar((sound * vol), pan);

	OffsetOut.ar(out, sound);
}).add;
)

a=Synth("bell-soft", [\freq, 880],)


// 1. define the synth
(
SynthDef(\bell, {
	|fs=1, t60=1, pitchy=1, amp=0.25, gate=1|
	var sig, exciter;
	//exciter = Impulse.ar(0);
	exciter = WhiteNoise.ar() * EnvGen.ar(Env.perc(0.001, 0.05), gate) * 0.25;
	sig = Klank.ar(
		`[
			[1, 2, 2.803, 3.871, 5.074, 7.81, 10.948, 14.421],   // freqs
			[1, 0.044, 0.891, 0.0891, 0.794, 0.1, 0.281, 0.079], // amplitudes
			[1, 0.205, 1, 0.196, 0.339, 0.047, 0.058, 0.047]*t60     // ring times
		],
		exciter,
		freqscale:fs*pitchy);
	sig = FreeVerb.ar(sig) * amp;
	DetectSilence.ar(sig, 0.001, 0.5, doneAction:2);
	Out.ar(0, sig!2);
}).add
)


// 2. Test a single note
x = Synth(\bell, [\fs, 60.midicps, \t60, 9.177, \pitchy, 8]);


// 3. Test different textures

// glokenspiel
(
Pbind(
	\instrument, \bell,
	\fs, Pseq( (60..72), 1).midicps,
	\t60, 6,
	\pitchy, 4,
	\dur, 0.25
).play;
)

// marimba
(
Pbind(
	\instrument, \bell,
	\fs, Pseq( (60..72), 1).midicps,
	\t60, 0.5,
	\pitchy, 1,
	\dur, 0.25
).play;
)



// From: https://sccode.org/1-4VL

(
SynthDef(\prayer_bell, { |outbus, t_trig = 1, sing_switch = 0, freq = 2434, amp = 0.5, decayscale = 1, lag = 10, i_doneAction = 0|
  var sig, input, first, freqscale, mallet, sing;
  freqscale = freq / 2434;
  freqscale = Lag3.kr(freqscale, lag);
  decayscale = Lag3.kr(decayscale, lag);

  mallet = LPF.ar(Trig.ar(t_trig, SampleDur.ir)!2, 10000 * freqscale);
  sing = LPF.ar(
    LPF.ar(
      {
        PinkNoise.ar * Integrator.kr(sing_switch * 0.001, 0.999).linexp(0, 1, 0.01, 1) * amp
      } ! 2,
      2434 * freqscale
    ) + Dust.ar(0.1), 10000 * freqscale
  ) * LFNoise1.kr(0.5).range(-45, -30).dbamp;
  input = mallet + (sing_switch.clip(0, 1) * sing);


  sig = DynKlank.ar(`[
    [
      (first = LFNoise1.kr(0.5).range(2424, 2444)) + Line.kr(20, 0, 0.5),
      first + LFNoise1.kr(0.5).range(1,3),
      LFNoise1.kr(1.5).range(5435, 5440) - Line.kr(35, 0, 1),
      LFNoise1.kr(1.5).range(5480, 5485) - Line.kr(10, 0, 0.5),
      LFNoise1.kr(2).range(8435, 8445) + Line.kr(15, 0, 0.05),
      LFNoise1.kr(2).range(8665, 8670),
      LFNoise1.kr(2).range(8704, 8709),
      LFNoise1.kr(2).range(8807, 8817),
      LFNoise1.kr(2).range(9570, 9607),
      LFNoise1.kr(2).range(10567, 10572) - Line.kr(20, 0, 0.05),
      LFNoise1.kr(2).range(10627, 10636) + Line.kr(35, 0, 0.05),
      LFNoise1.kr(2).range(14689, 14697) - Line.kr(10, 0, 0.05)
    ],
    [
      LFNoise1.kr(1).range(-10, -5).dbamp,
      LFNoise1.kr(1).range(-20, -10).dbamp,
      LFNoise1.kr(1).range(-12, -6).dbamp,
      LFNoise1.kr(1).range(-12, -6).dbamp,
      -20.dbamp,
      -20.dbamp,
      -20.dbamp,
      -25.dbamp,
      -10.dbamp,
      -20.dbamp,
      -20.dbamp,
      -25.dbamp
    ],
    [
      20 * freqscale.pow(0.2),
      20 * freqscale.pow(0.2),
      5,
      5,
      0.6,
      0.5,
      0.3,
      0.25,
      0.4,
      0.5,
      0.4,
      0.6
    ] * freqscale.reciprocal.pow(0.5)
  ], input, freqscale, 0, decayscale);
  DetectSilence.ar(sig, doneAction: i_doneAction);
  Out.ar(outbus, sig);
}).add;
)


(
Pdef(\bell_1,
  Pmono(\prayer_bell,
    \dur, Pseq([8, 20], inf),
    \freq, Pseq([2500, 500], inf),
    \amp, 0.5,
    \lag, 0,
    \trig, Pseq([0.1], inf) * Pwhite(0.5, 1, inf) * Pwrand([0, 1], [1, 5].normalizeSum, inf),
    \sing_switch, Pseq((0!4) ++ (1!4), inf)
  )
);

Pdef(\bell_2,
  Pmono(\prayer_bell,
    \dur, Pwhite(8, 20, inf),
    \trig, Pwhite(0.05, 0.09),
    \sing_switch, Pwrand([0, 1], [5, 3].normalizeSum, inf),
    \freq, Prand((240, 360 .. 2000), inf),
    \amp, 0.5
  )
);

Pdef(\bell_3,
  Ppar([
    Pmono(\prayer_bell,
      \freq, 100,
      \dur, 1,
      \trig, 0,
      \sing_switch, Pwrand([0, 1], [10, 3].normalizeSum, inf),
      \amp, Pwhite(0.1, 0.5)
    ),
    Pmono(\prayer_bell,
      \freq, 200,
      \dur, 1,
      \trig, 0,
      \sing_switch, Pwrand([0, 1], [10, 3].normalizeSum, inf),
      \amp, Pwhite(0.1, 0.5)
    ),
    Pmono(\prayer_bell,
      \freq, 300,
      \dur, 1,
      \trig, 0,
      \sing_switch, Pwrand([0, 1], [10, 3].normalizeSum, inf),
      \amp, Pwhite(0.1, 0.5)
    )
  ])
);

Pdef(\bell_1).play;
Pdef(\bell_2).play;
Pdef(\bell_3).play;
)