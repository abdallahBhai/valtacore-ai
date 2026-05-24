const projects = [
  {
    id: '01',
    name: 'Nexus Bank',
    category: 'Financial Platform',
    desc: 'A reimagined banking interface — where clarity meets institutional confidence.',
    year: '2024',
    seed: 'nexusbank',
  },
  {
    id: '02',
    name: 'Bloome',
    category: 'E-Commerce',
    desc: 'Luxury fashion retail elevated through a cinematic, editorial digital presence.',
    year: '2024',
    seed: 'bloome',
  },
  {
    id: '03',
    name: 'Meridian',
    category: 'SaaS Dashboard',
    desc: 'Enterprise analytics wrapped in a design that respects the intelligence of its users.',
    year: '2023',
    seed: 'meridian',
  },
  {
    id: '04',
    name: 'Pulse Health',
    category: 'Healthcare Portal',
    desc: 'Clinical precision meets human warmth — every interaction feels considered.',
    year: '2023',
    seed: 'pulsehealth',
  },
  {
    id: '05',
    name: 'Forge Creative',
    category: 'Agency Portfolio',
    desc: 'A digital canvas purpose-built for a world-class creative studio.',
    year: '2024',
    seed: 'forgecreative',
  },
  {
    id: '06',
    name: 'Orbit Logistics',
    category: 'Web Application',
    desc: 'Supply chain complexity distilled to a single, coherent interface.',
    year: '2024',
    seed: 'orbitlogistics',
  },
]

export default function Portfolio() {
  return (
    <section id="portfolio" className="bg-[#080808] py-28 px-10">
      {/* Section header */}
      <div className="flex items-center gap-6 mb-5">
        <span className="text-white/25 text-[11px] font-medium tracking-[0.3em] font-mono">02 /</span>
        <div className="flex-1 h-px bg-white/8" />
        <span className="text-white/25 text-[11px] font-medium tracking-[0.3em] font-mono">PORTFOLIO</span>
      </div>

      <div className="flex items-end justify-between mb-16">
        <h2 className="font-serif text-[clamp(3rem,6vw,5.5rem)] font-light leading-none">
          Selected<br /><em className="italic">Works</em>
        </h2>
        <p className="text-white/40 text-sm max-w-xs text-right leading-relaxed hidden md:block">
          A selection of projects built for clients who understand that design is a competitive advantage.
        </p>
      </div>

      {/* Grid */}
      <div className="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-3 gap-px bg-white/5">
        {projects.map((p) => (
          <article
            key={p.id}
            className="group relative bg-[#080808] overflow-hidden cursor-pointer"
          >
            {/* Image area */}
            <div className="relative overflow-hidden aspect-[4/3]">
              {/* Placeholder gradient pattern unique to each project */}
              <div
                className="w-full h-full transition-transform duration-700 group-hover:scale-105"
                style={{
                  background: `
                    radial-gradient(ellipse at ${parseInt(p.id) * 20}% ${parseInt(p.id) * 15}%, rgba(255,255,255,0.06) 0%, transparent 60%),
                    linear-gradient(${parseInt(p.id) * 45}deg, #0a0a0a 0%, #161616 50%, #0d0d0d 100%)
                  `,
                  backgroundSize: '100% 100%',
                }}
              >
                {/* Abstract grid lines inside card image */}
                <svg className="absolute inset-0 w-full h-full opacity-30" viewBox="0 0 400 300" fill="none">
                  <rect x="30" y="30" width="340" height="240" stroke="rgba(255,255,255,0.07)" strokeWidth="1" />
                  <line x1="0" y1="150" x2="400" y2="150" stroke="rgba(255,255,255,0.04)" strokeWidth="1" />
                  <line x1="200" y1="0" x2="200" y2="300" stroke="rgba(255,255,255,0.04)" strokeWidth="1" />
                  <circle cx="200" cy="150" r="60" stroke="rgba(255,255,255,0.06)" strokeWidth="1" />
                  <circle cx="200" cy="150" r="30" stroke="rgba(255,255,255,0.05)" strokeWidth="1" />
                  <circle cx="200" cy="150" r="5" fill="rgba(255,255,255,0.15)" />
                  {/* Project number watermark */}
                  <text x="24" y="268" fontFamily="monospace" fontSize="48" fill="rgba(255,255,255,0.04)" fontWeight="700">{p.id}</text>
                </svg>
              </div>

              {/* Hover overlay with arrow */}
              <div className="absolute inset-0 bg-white/0 group-hover:bg-white/[0.03] transition-all duration-300 flex items-center justify-center">
                <div className="w-12 h-12 rounded-full border border-white/0 group-hover:border-white/30 flex items-center justify-center opacity-0 group-hover:opacity-100 transition-all duration-300 -translate-y-2 group-hover:translate-y-0">
                  <svg viewBox="0 0 16 16" className="w-4 h-4 text-white -rotate-45" fill="none">
                    <path d="M3 8h10M9 4l4 4-4 4" stroke="currentColor" strokeWidth="1.5" strokeLinecap="round" strokeLinejoin="round" />
                  </svg>
                </div>
              </div>

              {/* Category tag */}
              <div className="absolute top-4 left-4">
                <span className="text-[10px] font-medium tracking-[0.2em] text-white/50 font-mono">{p.category.toUpperCase()}</span>
              </div>
            </div>

            {/* Card body */}
            <div className="p-6 border-t border-white/6">
              <div className="flex items-start justify-between">
                <div>
                  <h3 className="font-serif text-2xl font-light mb-1.5 group-hover:text-white/90 transition-colors">
                    {p.name}
                  </h3>
                  <p className="text-white/40 text-[13px] leading-relaxed">{p.desc}</p>
                </div>
                <span className="text-white/20 text-[11px] font-mono mt-1 flex-shrink-0 ml-4">{p.year}</span>
              </div>
            </div>
          </article>
        ))}
      </div>

      {/* Bottom CTA */}
      <div className="mt-16 flex justify-center">
        <a
          href="#contact"
          className="flex items-center gap-3 text-white/40 hover:text-white text-sm border-b border-white/15 hover:border-white/50 pb-1 transition-all duration-300"
        >
          Start your project
          <svg viewBox="0 0 14 14" className="w-3.5 h-3.5" fill="none">
            <path d="M2 7h10M8 3l4 4-4 4" stroke="currentColor" strokeWidth="1.5" strokeLinecap="round" strokeLinejoin="round" />
          </svg>
        </a>
      </div>
    </section>
  )
}
