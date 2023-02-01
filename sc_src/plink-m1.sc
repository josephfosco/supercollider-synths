(
SynthDef("plink-m1", {
	arg freq=440, vol=1, gate=1.0;

	var sound, env;

	env = EnvGen.kr(Env.perc(attackTime: 0.01, releaseTime: 0.3), gate: gate,
		            levelScale: (vol * 0.4));

	sound = SinOsc.ar(freq: freq) +
		    SinOsc.ar(freq: (freq * 3)) * (1 / 3) +
		    SinOsc.ar(freq: (freq * 5.1)) * (1 / 5) +
	        SinOsc.ar(freq: (freq * 6.1)) * (1 / 6) +
		    SinOsc.ar(freq: (freq * 7.1)) * (1 / 8) +
		    SinOsc.ar(freq: (freq * 8)) * (1 / 8);

		OffsetOut.ar([0, 1], sound * env);
}
)
).add;

a=Synth("plink-m1")

a.set("gate", 0)

a.set("gate", 1)

a.set("done", Done.freeSelf)

a.free
