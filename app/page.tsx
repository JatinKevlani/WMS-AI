import Link from "next/link";
import { ArrowRight, Box, Activity, CheckCircle2 } from "lucide-react";

export default function LoginPage() {
  return (
    <div className="min-h-screen flex w-full bg-surface">
      {/* Left side: Login Form */}
      <div className="w-full lg:w-[45%] flex flex-col justify-center px-8 md:px-16 lg:px-24">
        <div className="max-w-md w-full mx-auto">
          {/* Logo or Brand */}
          <div className="flex items-center gap-2 mb-12">
            <div className="bg-primary/10 p-2 rounded-lg">
              <Box className="w-6 h-6 text-primary" />
            </div>
            <span className="font-semibold text-on-surface tracking-tight text-xl">
              WMS-AI
            </span>
          </div>

          <h1 className="text-4xl text-on-surface font-semibold tracking-tight mb-2">
            Welcome back.
          </h1>
          <p className="text-on-surface-variant text-base mb-10">
            Log in to manage your warehouse intelligence.
          </p>

          <form className="space-y-6">
            <div className="space-y-2">
              <label
                htmlFor="email"
                className="block text-sm font-medium text-on-surface"
              >
                Email Address
              </label>
              <input
                id="email"
                type="email"
                className="w-full bg-surface-container-lowest border border-outline-variant/30 rounded-xl px-4 py-3 text-on-surface placeholder:text-on-surface-variant focus:outline-none focus:ring-2 focus:ring-primary/20 focus:border-primary transition-all"
                placeholder="foreman@wms-ai.com"
                required
              />
            </div>

            <div className="space-y-2">
              <div className="flex justify-between items-center">
                <label
                  htmlFor="password"
                  className="block text-sm font-medium text-on-surface"
                >
                  Password
                </label>
                <Link
                  href="/forgot-password"
                  className="text-sm text-primary hover:text-primary-container transition-colors"
                >
                  Forgot password?
                </Link>
              </div>
              <input
                id="password"
                type="password"
                className="w-full bg-surface-container-lowest border border-outline-variant/30 rounded-xl px-4 py-3 text-on-surface placeholder:text-on-surface-variant focus:outline-none focus:ring-2 focus:ring-primary/20 focus:border-primary transition-all"
                placeholder="••••••••"
                required
              />
            </div>

            <Link href="/dashboard" className="block w-full">
              <button
                type="button"
                className="w-full group flex items-center justify-center gap-2 bg-gradient-to-br from-primary to-primary-container text-white rounded-xl px-4 py-3 font-medium transition-all hover:shadow-[0_20px_40px_-10px_rgba(53,37,205,0.4)] hover:scale-[1.01]"
              >
                Sign In to Platform
                <ArrowRight className="w-4 h-4 transition-transform group-hover:translate-x-1" />
              </button>
            </Link>
          </form>

          <p className="mt-8 text-center text-sm text-on-surface-variant">
            Join 2,400+ logistics experts using The Digital Foreman.
          </p>
        </div>
      </div>

      {/* Right side: Intelligence View */}
      <div className="hidden lg:flex w-[55%] bg-surface-container-low p-8 relative overflow-hidden">
        {/* Abstract background elements */}
        <div className="absolute top-0 right-0 w-[800px] h-[800px] bg-gradient-to-b from-primary/5 to-transparent rounded-full blur-3xl -translate-y-1/2 translate-x-1/3" />
        
        <div className="relative w-full h-full rounded-2xl bg-surface/40 border border-white/20 backdrop-blur-xl shadow-[0_20px_40px_-10px_rgba(15,23,42,0.08)] flex flex-col justify-between p-12 overflow-hidden">
          {/* Top badge */}
          <div className="self-start inline-flex flex-col gap-4">
            <span className="inline-flex items-center gap-2 bg-white/60 backdrop-blur-md border border-white/40 px-4 py-2 rounded-full text-sm font-medium text-primary shadow-sm">
              <Activity className="w-4 h-4" />
              Live System State
            </span>
          </div>

          <div className="space-y-6 max-w-xl">
            <h2 className="text-5xl font-semibold leading-tight text-on-surface tracking-tight">
              Predictive Load Balancing is currently optimizing <span className="text-primary">14 active routes.</span>
            </h2>

            <div className="grid grid-cols-2 gap-4 mt-8">
              <div className="bg-white/60 backdrop-blur-md p-6 rounded-2xl border border-white/40 shadow-sm transition-all hover:-translate-y-1">
                <p className="text-sm text-on-surface-variant mb-2">Efficiency Gain</p>
                <div className="flex items-end gap-2">
                  <span className="text-4xl font-semibold text-primary">+24.8%</span>
                  <span className="text-sm text-tertiary font-medium bg-tertiary/10 px-2 py-0.5 rounded-full mb-1">+12% vs last week</span>
                </div>
              </div>

              <div className="bg-white/60 backdrop-blur-md p-6 rounded-2xl border border-white/40 shadow-sm transition-all hover:-translate-y-1">
                <p className="text-sm text-on-surface-variant mb-2">Active Automation</p>
                <div className="flex items-start gap-3">
                  <div className="bg-emerald-500/10 p-2 rounded-full shrink-0">
                    <CheckCircle2 className="w-5 h-5 text-emerald-600" />
                  </div>
                  <div>
                    <span className="block font-semibold text-on-surface text-lg">Sector G</span>
                    <span className="text-sm text-on-surface-variant">Automated picking active and optimal.</span>
                  </div>
                </div>
              </div>
            </div>
          </div>
        </div>
      </div>
    </div>
  );
}
