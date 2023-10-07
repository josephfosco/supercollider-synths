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


// from: https://sccode.org/1-5cP

(
SynthDef.new(
	\synth_simpleSine,
	{
		arg freq=220, rate=0.1, pan=0.0, amp=1.0, dur=1.0, lfor1=0.08, lfor2=0.05, nl=0.5, filt=5000;
		var sig, sub, lfo1, lfo2, env, noise;

		lfo1  = SinOsc.kr(lfor1, 0.5, 1, 0);
		lfo2  = SinOsc.kr(lfor2, 0, 1, 0);
		sig   = SinOscFB.ar(freq, lfo1, 1, 0);
		sub   = SinOscFB.ar(freq*0.25, lfo2, 1, 0);
		env   = Line.kr(1, 0, dur*4.0, doneAction: Done.freeSelf);
		noise = PinkNoise.ar(nl, 0);
		sig   = (sig + sub) * env;
	  //sig   = BLowPass4.ar(sig, filt);
		sig   = MoogFF.ar(sig, filt * 0.5, 0, 0, 1, 0);
		sig   = Pan2.ar(sig, pan, amp);
	    sig   = FreeVerb2.ar(sig[0], sig[1], 0.5, 0.99, 0.9);
		Out.ar(0, sig * 0.6);
	}
).add;
"Synth Added!".postln;
)

(
SynthDef.new(
	\bell2,
	{
		arg freq=220, rate=0.1, pan=0.0, amp=1.0, dur=1.0, lfor1=0.08, lfor2=0.05, nl=0.5, filt=5000;
		var sig, sub, lfo1, lfo2, env, noise;

		lfo1  = SinOsc.kr(lfor1, 0.5, 1, 0);
		sig   = SinOscFB.ar(freq, lfo1, 1, 0);
		env   = Line.kr(1, 0, dur*4.0, doneAction: Done.freeSelf);
		sig   = (sig) * env;
		sig   = MoogFF.ar(sig, filt * 0.5, 0, 0, 1, 0);
		sig   = Pan2.ar(sig, pan, amp);
	    sig   = FreeVerb2.ar(sig[0], sig[1], 0.5, 0.99, 0.9);
		Out.ar(0, sig * 0.6);
	}
).add;
"Synth Added!".postln;
)

a=Synth("synth_simpleSine", [\freq, 880, \dur, 1.0, \amp, 0.7],)


b=Synth("bell2", [\freq, 880, \dur, 1.0, \amp, 0.7],)




(
Pdef (
	\seq1,
	Pbind(
		\instrument, \synth_simpleSine,
		\dur     , 4.0,
		\amp     , Pwhite(0.7, 0.8, inf),
		\midinote, Pseq([60, 62, 64, 67, 60, 62, 64, 71, 72], inf),
		\harmonic, Pseq([1, 2, 4], inf),
		\pan     , Pwhite(-1.0, 1.0, inf),
		\lfor1   , Pwhite(0.001, 5.5, inf),
		\lfor2   , Pwhite(0.001, 0.1, inf),
		\nl      , Pwhite(0.3, 0.9, inf),
		\filt    , Pwhite(250, 1000, inf),
	);
).play;
"Sequence 1 Started".postln;

Pdef (
	\seq2,
	Pbind(
		\instrument, \synth_simpleSine,
		\dur     , 0.8,
		\amp     , Pwhite(0.01, 0.4, inf),
		\midinote, (Pseq([60, 62, 64, 67, 60, 62, 64, 71, 74], inf)+24),
		\harmonic, Pseq([1, 2, 4], inf),
		\pan     , Pwhite(-1.0, 1.0, inf),
		\lfor1   , Pwhite(0.001, 10.1, inf),
		\lfor2   , Pwhite(0.001, 0.1, inf),
		\nl      , Pwhite(0.8, 1.0, inf),
		\filt    , Pwhite(500, 2500, inf),
	);
).play;
"Sequence 2 Started".postln;

Pdef (
	\seq3,
	Pbind(
		\instrument, \synth_simpleSine,
		\dur     , 8.0,
		\amp     , Pwhite(0.01, 0.4, inf),
		\midinote, (Pxrand([60, 62, 64, 67, 60, 62, 64, 71, 74], inf)),
		\harmonic, Pseq([1, 2, 2], inf),
		\pan     , Pxrand([-1.0, 1.0], inf),
		\lfor1   , Pwhite(0.001, 0.1, inf),
		\lfor2   , Pwhite(0.001, 0.2, inf),
		\nl      , Pwhite(0.8, 1.0, inf),
		\filt    , Pwhite(500, 2500, inf),
	);
).play;
"Sequence 3 Started".postln;
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
