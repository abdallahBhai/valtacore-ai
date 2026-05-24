export default function Hero() {
  return (
    <section id="about" className="flex h-screen min-h-[600px]">

      {/* ── LEFT PANEL ─────────────────────────────────────────── */}
      <div className="w-[45%] bg-black flex flex-col justify-between px-12 py-10 pt-32 border-r border-white/5">
        <div>
          {/* Slide counter */}
          <div className="flex items-baseline gap-1 mb-10">
            <span className="font-serif text-5xl font-light text-white tracking-tight">01</span>
            <span className="font-serif text-5xl font-light text-white/25 tracking-tight">/01</span>
          </div>

          {/* Prev / Next arrows */}
          <div className="flex gap-2.5 mb-12">
            <button
              aria-label="Previous"
              className="w-9 h-9 rounded-full border border-white/20 flex items-center justify-center text-white/60 hover:text-white hover:border-white/50 hover:bg-white/5 transition-all duration-200"
            >
              <svg viewBox="0 0 14 14" className="w-3.5 h-3.5" fill="none">
                <path d="M9 2L4 7l5 5" stroke="currentColor" strokeWidth="1.5" strokeLinecap="round" strokeLinejoin="round" />
              </svg>
            </button>
            <button
              aria-label="Next"
              className="w-9 h-9 rounded-full border border-white/20 flex items-center justify-center text-white/60 hover:text-white hover:border-white/50 hover:bg-white/5 transition-all duration-200"
            >
              <svg viewBox="0 0 14 14" className="w-3.5 h-3.5" fill="none">
                <path d="M5 2l5 5-5 5" stroke="currentColor" strokeWidth="1.5" strokeLinecap="round" strokeLinejoin="round" />
              </svg>
            </button>
          </div>

          {/* Headline */}
          <h1 className="font-serif text-[clamp(2.6rem,5vw,4.5rem)] leading-[1.05] font-light mb-8">
            We don&apos;t make
            <br />
            <em className="italic">websites.</em>
            <br />
            We create <em className="italic">art.</em>
          </h1>

          {/* Body */}
          <p className="text-white/45 text-[14px] leading-relaxed max-w-sm mb-12 font-light">
            Valtacore AI engineers digital experiences that transcend conventional design.
            Each project is a singular work — precision-crafted, obsessively refined,
            built to outlast trends.
          </p>

          {/* CTAs */}
          <div className="flex flex-wrap gap-3">
            <a
              href="#portfolio"
              className="px-7 py-3.5 bg-white text-black text-[13px] font-semibold hover:bg-white/90 active:scale-[0.98] transition-all duration-200"
            >
              View Portfolio
            </a>
            <a
              href="#services"
              className="px-7 py-3.5 border border-white/20 text-white text-[13px] font-medium hover:bg-white/5 hover:border-white/40 active:scale-[0.98] transition-all duration-200"
            >
              Our Services ✦
            </a>
          </div>
        </div>

        {/* Bottom contact strip */}
        <div className="border-t border-white/8 pt-6">
          <p className="text-white/25 text-[10px] font-medium tracking-[0.25em] mb-2">READY TO BEGIN</p>
          <p className="text-white/55 text-[13px]">hello@valtacore.ai</p>
        </div>
      </div>

      {/* ── RIGHT PANEL — Editorial geometric visual ───────────── */}
      <div
        className="flex-1 bg-[#0f0f0f] relative overflow-hidden"
        style={{
          backgroundImage: 'radial-gradient(rgba(255,255,255,0.055) 1px, transparent 1px)',
          backgroundSize: '38px 38px',
        }}
      >
        {/* Corner labels */}
        <div className="absolute top-7 left-7 text-white/18 text-[9px] tracking-[0.3em] font-mono z-10">
          SCULPT.1
        </div>
        <div className="absolute top-7 right-7 text-white/18 text-[9px] tracking-[0.3em] font-mono z-10">
          VALTACORE AI
        </div>

        {/* SVG geometric diagram */}
        <svg
          className="absolute inset-0 w-full h-full"
          viewBox="0 0 700 600"
          fill="none"
          xmlns="http://www.w3.org/2000/svg"
          preserveAspectRatio="xMidYMid meet"
        >
          {/* Outer corner dots */}
          <circle cx="80"  cy="70"  r="3" fill="rgba(255,255,255,0.28)" />
          <circle cx="620" cy="70"  r="3" fill="rgba(255,255,255,0.28)" />
          <circle cx="80"  cy="530" r="3" fill="rgba(255,255,255,0.28)" />
          <circle cx="620" cy="530" r="3" fill="rgba(255,255,255,0.28)" />

          {/* Mid-edge dots */}
          <circle cx="80"  cy="300" r="2.5" fill="rgba(255,255,255,0.18)" />
          <circle cx="620" cy="300" r="2.5" fill="rgba(255,255,255,0.18)" />
          <circle cx="350" cy="70"  r="2.5" fill="rgba(255,255,255,0.18)" />
          <circle cx="350" cy="530" r="2.5" fill="rgba(255,255,255,0.18)" />

          {/* Concentric circles */}
          <circle cx="350" cy="300" r="90"  stroke="rgba(255,255,255,0.13)" strokeWidth="1" />
          <circle cx="350" cy="300" r="160" stroke="rgba(255,255,255,0.08)" strokeWidth="1" />
          <circle cx="350" cy="300" r="240" stroke="rgba(255,255,255,0.045)" strokeWidth="1" />
          <circle cx="350" cy="300" r="330" stroke="rgba(255,255,255,0.025)" strokeWidth="1" />

          {/* Cross lines through center */}
          <line x1="350" y1="140" x2="350" y2="460" stroke="rgba(255,255,255,0.07)" strokeWidth="1" />
          <line x1="190" y1="300" x2="510" y2="300" stroke="rgba(255,255,255,0.07)" strokeWidth="1" />

          {/* Diagonal lines */}
          <line x1="236" y1="186" x2="464" y2="414" stroke="rgba(255,255,255,0.04)" strokeWidth="0.8" />
          <line x1="464" y1="186" x2="236" y2="414" stroke="rgba(255,255,255,0.04)" strokeWidth="0.8" />

          {/* Intersection dots on inner circle */}
          <circle cx="350" cy="210" r="3.5" fill="rgba(255,255,255,0.45)" />
          <circle cx="350" cy="390" r="3.5" fill="rgba(255,255,255,0.45)" />
          <circle cx="260" cy="300" r="3.5" fill="rgba(255,255,255,0.45)" />
          <circle cx="440" cy="300" r="3.5" fill="rgba(255,255,255,0.45)" />

          {/* Intersection dots on second circle */}
          <circle cx="350" cy="140" r="2.5" fill="rgba(255,255,255,0.3)" />
          <circle cx="350" cy="460" r="2.5" fill="rgba(255,255,255,0.3)" />
          <circle cx="190" cy="300" r="2.5" fill="rgba(255,255,255,0.3)" />
          <circle cx="510" cy="300" r="2.5" fill="rgba(255,255,255,0.3)" />

          {/* Center point */}
          <circle cx="350" cy="300" r="12" stroke="rgba(255,255,255,0.18)" strokeWidth="1" fill="none" />
          <circle cx="350" cy="300" r="4"  fill="rgba(255,255,255,0.5)" />

          {/* Annotation: Left */}
          <line x1="260" y1="300" x2="185" y2="255" stroke="rgba(255,255,255,0.1)" strokeWidth="0.8" />
          <line x1="185" y1="255" x2="88"  y2="255" stroke="rgba(255,255,255,0.1)" strokeWidth="0.8" />
          <text x="90" y="247" fontFamily="monospace" fontSize="7" fill="rgba(255,255,255,0.22)" letterSpacing="1.5">DESIGN CORE</text>
          <text x="90" y="262" fontFamily="monospace" fontSize="6" fill="rgba(255,255,255,0.14)" letterSpacing="1">CLARITY / PRECISION</text>

          {/* Annotation: Right */}
          <line x1="440" y1="300" x2="515" y2="255" stroke="rgba(255,255,255,0.1)" strokeWidth="0.8" />
          <line x1="515" y1="255" x2="612" y2="255" stroke="rgba(255,255,255,0.1)" strokeWidth="0.8" />
          <text x="518" y="247" fontFamily="monospace" fontSize="7" fill="rgba(255,255,255,0.22)" letterSpacing="1.5">AI SYNTHESIS</text>
          <text x="518" y="262" fontFamily="monospace" fontSize="6" fill="rgba(255,255,255,0.14)" letterSpacing="1">CURVES v2.1</text>

          {/* Annotation: Top */}
          <line x1="350" y1="210" x2="285" y2="165" stroke="rgba(255,255,255,0.1)" strokeWidth="0.8" />
          <line x1="285" y1="165" x2="168" y2="165" stroke="rgba(255,255,255,0.1)" strokeWidth="0.8" />
          <text x="172" y="157" fontFamily="monospace" fontSize="7" fill="rgba(255,255,255,0.22)" letterSpacing="1.5">MACHINE × HUMAN</text>
          <text x="172" y="172" fontFamily="monospace" fontSize="6" fill="rgba(255,255,255,0.14)" letterSpacing="1">AS MIND</text>

          {/* Annotation: Bottom */}
          <line x1="350" y1="390" x2="415" y2="435" stroke="rgba(255,255,255,0.1)" strokeWidth="0.8" />
          <line x1="415" y1="435" x2="532" y2="435" stroke="rgba(255,255,255,0.1)" strokeWidth="0.8" />
          <text x="536" y="427" fontFamily="monospace" fontSize="7" fill="rgba(255,255,255,0.22)" letterSpacing="1.5">BOLD STROKES</text>
          <text x="536" y="442" fontFamily="monospace" fontSize="6" fill="rgba(255,255,255,0.14)" letterSpacing="1">AR × 2</text>

          {/* Corner label text */}
          <text x="84"  y="535" fontFamily="monospace" fontSize="7" fill="rgba(255,255,255,0.18)" letterSpacing="1.5">WEB SYNTHESIS v3.0</text>
          <text x="490" y="535" fontFamily="monospace" fontSize="7" fill="rgba(255,255,255,0.18)" letterSpacing="1.5">CONTROLLER v2.8</text>
          <text x="84"  y="75"  fontFamily="monospace" fontSize="7" fill="rgba(255,255,255,0.18)" letterSpacing="1.5">IN THE TRADITION</text>
          <text x="490" y="75"  fontFamily="monospace" fontSize="7" fill="rgba(255,255,255,0.18)" letterSpacing="1.5">CODE ARTISTRY</text>
        </svg>
      </div>
    </section>
  )
}
