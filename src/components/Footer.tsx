const footerLinks = {
  Work: ['Portfolio', 'Case Studies', 'Process'],
  Services: ['Web Design', 'Development', 'AI Integration'],
  Company: ['About', 'Pricing', 'Contact'],
}

export default function Footer() {
  return (
    <footer id="contact" className="bg-[#080808] border-t border-white/8">
      {/* Top CTA band */}
      <div className="border-b border-white/8 px-10 py-16">
        <div className="flex flex-col md:flex-row md:items-end justify-between gap-10">
          <div>
            <p className="text-white/25 text-[11px] font-mono tracking-[0.3em] mb-5">04 / CONTACT</p>
            <h2 className="font-serif text-[clamp(2.5rem,5vw,5rem)] font-light leading-none">
              Let&apos;s make<br /><em className="italic">something rare.</em>
            </h2>
          </div>
          <a
            href="mailto:hello@valtacore.ai"
            className="group flex items-center gap-3 text-white/50 hover:text-white font-serif text-xl italic border-b border-white/15 hover:border-white/60 pb-1 transition-all duration-300 whitespace-nowrap"
          >
            hello@valtacore.ai
            <svg viewBox="0 0 16 16" className="w-4 h-4 -rotate-45 group-hover:translate-x-0.5 group-hover:-translate-y-0.5 transition-transform" fill="none">
              <path d="M3 8h10M9 4l4 4-4 4" stroke="currentColor" strokeWidth="1.5" strokeLinecap="round" strokeLinejoin="round" />
            </svg>
          </a>
        </div>
      </div>

      {/* Main footer body */}
      <div className="px-10 py-14 grid grid-cols-2 md:grid-cols-4 gap-12">
        {/* Brand */}
        <div className="col-span-2 md:col-span-1">
          <div className="flex items-center gap-3 mb-4">
            <div className="w-7 h-7 rounded-full bg-white flex items-center justify-center">
              <svg viewBox="0 0 14 14" className="w-3.5 h-3.5" fill="none">
                <path d="M7 1.5L8.5 5.5H12.5L9.5 8L10.5 12L7 9.5L3.5 12L4.5 8L1.5 5.5H5.5L7 1.5Z" fill="#080808" />
              </svg>
            </div>
            <span className="text-white text-[13px] font-semibold tracking-[0.12em]">VALTACORE AI</span>
          </div>
          <p className="text-white/35 text-[13px] leading-relaxed font-light">
            We don&apos;t make websites.<br />We create art.
          </p>
        </div>

        {/* Link groups */}
        {Object.entries(footerLinks).map(([group, links]) => (
          <div key={group}>
            <p className="text-white/25 text-[10px] font-medium tracking-[0.3em] mb-5">{group.toUpperCase()}</p>
            <ul className="flex flex-col gap-3">
              {links.map((link) => (
                <li key={link}>
                  <a
                    href={`#${link.toLowerCase().replace(/\s/g, '-')}`}
                    className="text-white/45 hover:text-white text-[13px] transition-colors duration-200"
                  >
                    {link}
                  </a>
                </li>
              ))}
            </ul>
          </div>
        ))}
      </div>

      {/* Bottom bar */}
      <div className="px-10 py-5 border-t border-white/6 flex flex-col sm:flex-row items-center justify-between gap-3">
        <p className="text-white/20 text-[11px] tracking-wide">
          © {new Date().getFullYear()} Valtacore AI. All rights reserved.
        </p>
        <div className="flex gap-5">
          {['Twitter', 'LinkedIn', 'Dribbble'].map((s) => (
            <a
              key={s}
              href="#"
              className="text-white/25 hover:text-white text-[11px] tracking-wide transition-colors duration-200"
            >
              {s}
            </a>
          ))}
        </div>
      </div>
    </footer>
  )
}
