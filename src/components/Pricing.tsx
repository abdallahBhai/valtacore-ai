const plans = [
  {
    id: '01',
    name: 'STARTER',
    price: '$2,999',
    cycle: 'one-time',
    tagline: 'Your digital debut, done right.',
    features: [
      'Single-page landing site',
      'Responsive across all devices',
      'Custom typography & layout',
      'Contact form integration',
      'Basic on-page SEO',
      '14-day delivery',
    ],
    cta: 'Start a Project',
    highlighted: false,
  },
  {
    id: '02',
    name: 'STUDIO',
    price: '$7,999',
    cycle: 'one-time',
    tagline: 'A full website that works as hard as you do.',
    features: [
      'Up to 10 custom pages',
      'Headless CMS integration',
      'Micro-animations & transitions',
      'Performance-optimized build',
      'Advanced SEO + analytics',
      'Figma source files included',
      '28-day delivery',
    ],
    cta: 'Get Started',
    highlighted: true,
  },
  {
    id: '03',
    name: 'ENTERPRISE',
    price: 'Custom',
    cycle: 'scoped per project',
    tagline: 'Full-stack. AI-integrated. Obsessively crafted.',
    features: [
      'Full-stack web application',
      'AI feature integration',
      'Custom design system',
      'Auth, DB, API architecture',
      'Load testing & security audit',
      'Dedicated project manager',
      'Ongoing support retainer',
    ],
    cta: 'Let\'s Talk',
    highlighted: false,
  },
]

export default function Pricing() {
  return (
    <section id="pricing" className="bg-[#000000] py-28 px-10">
      {/* Section header */}
      <div className="flex items-center gap-6 mb-5">
        <span className="text-white/25 text-[11px] font-medium tracking-[0.3em] font-mono">03 /</span>
        <div className="flex-1 h-px bg-white/8" />
        <span className="text-white/25 text-[11px] font-medium tracking-[0.3em] font-mono">PRICING</span>
      </div>

      <div className="flex items-end justify-between mb-16">
        <h2 className="font-serif text-[clamp(3rem,6vw,5.5rem)] font-light leading-none">
          Transparent<br /><em className="italic">Pricing</em>
        </h2>
        <p className="text-white/40 text-sm max-w-xs text-right leading-relaxed hidden md:block">
          No surprises. No bloat. You know exactly what you're getting and when you'll get it.
        </p>
      </div>

      {/* Cards */}
      <div className="grid grid-cols-1 md:grid-cols-3 gap-5">
        {plans.map((plan) => (
          <div
            key={plan.id}
            className={`relative flex flex-col p-8 border transition-all duration-300 hover:-translate-y-1 ${
              plan.highlighted
                ? 'border-white/35 bg-white/[0.03]'
                : 'border-white/10 bg-transparent hover:border-white/20'
            }`}
          >
            {/* Top bar accent */}
            <div
              className={`absolute top-0 left-8 right-8 h-px ${
                plan.highlighted ? 'bg-white' : 'bg-white/20'
              }`}
            />

            {/* Plan number */}
            <span className="text-white/25 text-[10px] font-mono tracking-[0.3em] mb-5">{plan.id}</span>

            {/* Name + tagline */}
            <h3 className="text-[11px] font-semibold tracking-[0.3em] text-white mb-2">{plan.name}</h3>
            <p className="text-white/40 text-[13px] leading-snug mb-8 font-light">{plan.tagline}</p>

            {/* Price */}
            <div className="mb-8">
              <div className="font-serif text-5xl font-light text-white leading-none mb-1">
                {plan.price}
              </div>
              <div className="text-white/30 text-[11px] tracking-widest mt-2">{plan.cycle.toUpperCase()}</div>
            </div>

            {/* Divider */}
            <div className="h-px bg-white/8 mb-8" />

            {/* Features */}
            <ul className="flex flex-col gap-3.5 flex-1 mb-10">
              {plan.features.map((f) => (
                <li key={f} className="flex items-start gap-3 text-[13px] text-white/55">
                  <span className="text-white/30 mt-0.5 flex-shrink-0">—</span>
                  {f}
                </li>
              ))}
            </ul>

            {/* CTA */}
            <a
              href="#contact"
              className={`flex items-center justify-between px-5 py-3.5 text-[13px] font-semibold transition-all duration-200 active:scale-[0.98] group ${
                plan.highlighted
                  ? 'bg-white text-black hover:bg-white/90'
                  : 'border border-white/20 text-white hover:border-white/50 hover:bg-white/5'
              }`}
            >
              {plan.cta}
              <svg viewBox="0 0 12 12" className="w-3 h-3 group-hover:translate-x-0.5 transition-transform" fill="none">
                <path d="M2 6h8M6.5 2.5L10 6l-3.5 3.5" stroke="currentColor" strokeWidth="1.5" strokeLinecap="round" strokeLinejoin="round" />
              </svg>
            </a>
          </div>
        ))}
      </div>

      {/* Fine print */}
      <p className="text-center text-white/20 text-xs mt-10 tracking-wide">
        All prices in USD. Rush delivery and retainer options available on request.
      </p>
    </section>
  )
}
