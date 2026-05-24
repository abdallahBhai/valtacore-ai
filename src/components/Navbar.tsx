'use client'

import { useState, useEffect } from 'react'
import Link from 'next/link'

const navLinks = ['Portfolio', 'Services', 'Pricing', 'About', 'Contact']

export default function Navbar() {
  const [scrolled, setScrolled] = useState(false)
  const [menuOpen, setMenuOpen] = useState(false)

  useEffect(() => {
    const onScroll = () => setScrolled(window.scrollY > 30)
    window.addEventListener('scroll', onScroll)
    return () => window.removeEventListener('scroll', onScroll)
  }, [])

  return (
    <header className="fixed top-0 left-0 right-0 z-50 flex justify-center pt-5 px-5">
      {/* ── Main pill bar ─────────────────────────────────────── */}
      <nav
        className={`w-full max-w-4xl flex items-center rounded-[22px] border transition-all duration-500 ${
          scrolled
            ? 'bg-black/90 border-white/12 backdrop-blur-2xl shadow-[0_8px_40px_rgba(0,0,0,0.6)]'
            : 'bg-black/50 border-white/8 backdrop-blur-xl'
        }`}
      >
        {/* ── Logo segment ─────────────── */}
        <div className="flex items-center gap-3 px-5 py-3.5 flex-shrink-0">
          <div className="w-7 h-7 rounded-full bg-white flex items-center justify-center flex-shrink-0">
            <svg viewBox="0 0 14 14" className="w-3.5 h-3.5" fill="none">
              <path
                d="M7 1.5L8.5 5.5H12.5L9.5 8L10.5 12L7 9.5L3.5 12L4.5 8L1.5 5.5H5.5L7 1.5Z"
                fill="#080808"
              />
            </svg>
          </div>
          <div>
            <div className="text-white text-[13px] font-semibold tracking-[0.12em] leading-none">
              VALTACORE
            </div>
            <div className="text-white/35 text-[9px] font-medium tracking-[0.2em] mt-0.5">AI</div>
          </div>
        </div>

        {/* ── Divider ─────────────────── */}
        <div className="w-px h-9 bg-white/10 flex-shrink-0" />

        {/* ── Nav links segment ───────── */}
        <div className="hidden md:flex items-center gap-0.5 flex-1 px-3 py-2">
          {navLinks.map((item) => (
            <Link
              key={item}
              href={`#${item.toLowerCase()}`}
              className="group relative px-4 py-2 text-[13px] font-medium text-white/50 hover:text-white transition-colors duration-200 rounded-xl hover:bg-white/5"
            >
              {item}
            </Link>
          ))}
        </div>

        {/* ── Divider ─────────────────── */}
        <div className="hidden md:block w-px h-9 bg-white/10 flex-shrink-0" />

        {/* ── CTA segment ─────────────── */}
        <div className="flex items-center px-3 py-2.5 gap-3 flex-shrink-0 ml-auto md:ml-0">
          <Link
            href="#contact"
            className="flex items-center gap-2 px-5 py-2 bg-white text-black text-[13px] font-semibold rounded-xl hover:bg-white/90 active:scale-95 transition-all duration-200 whitespace-nowrap"
          >
            Get a Quote
            <svg viewBox="0 0 12 12" className="w-3 h-3" fill="none">
              <path d="M2 6h8M6.5 2.5L10 6l-3.5 3.5" stroke="currentColor" strokeWidth="1.5" strokeLinecap="round" strokeLinejoin="round" />
            </svg>
          </Link>

          {/* Mobile menu toggle */}
          <button
            onClick={() => setMenuOpen(!menuOpen)}
            className="md:hidden flex flex-col gap-1.5 w-8 h-8 items-center justify-center"
            aria-label="Toggle menu"
          >
            <span className={`block w-5 h-px bg-white transition-all duration-300 ${menuOpen ? 'rotate-45 translate-y-[2.5px]' : ''}`} />
            <span className={`block w-5 h-px bg-white transition-all duration-300 ${menuOpen ? '-rotate-45 -translate-y-[2.5px]' : ''}`} />
          </button>
        </div>
      </nav>

      {/* ── Mobile dropdown ──────────────────────────────────── */}
      {menuOpen && (
        <div className="md:hidden absolute top-[76px] left-5 right-5 bg-black/95 backdrop-blur-xl border border-white/10 rounded-2xl p-4 flex flex-col gap-1">
          {navLinks.map((item) => (
            <Link
              key={item}
              href={`#${item.toLowerCase()}`}
              onClick={() => setMenuOpen(false)}
              className="px-4 py-3 text-sm text-white/60 hover:text-white hover:bg-white/5 rounded-xl transition-all"
            >
              {item}
            </Link>
          ))}
        </div>
      )}
    </header>
  )
}
