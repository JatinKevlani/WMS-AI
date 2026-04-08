import Link from "next/link";
import { ArrowLeft, Box } from "lucide-react";

export default function ForgotPasswordPage() {
  return (
    <div className="min-h-screen flex w-full bg-surface items-center justify-center p-8">
      <div className="max-w-md w-full mx-auto relative z-10">
        <Link href="/" className="inline-flex items-center gap-2 text-sm text-on-surface-variant hover:text-primary mb-8 transition-colors">
          <ArrowLeft className="w-4 h-4" />
          Back to login
        </Link>
        <div className="flex items-center gap-2 mb-8">
          <div className="bg-primary/10 p-2 rounded-lg">
            <Box className="w-6 h-6 text-primary" />
          </div>
          <span className="font-semibold text-on-surface tracking-tight text-xl">
            WMS-AI
          </span>
        </div>
        <h1 className="text-4xl text-on-surface font-semibold tracking-tight mb-2">
          Forgot Password
        </h1>
        <p className="text-on-surface-variant text-base mb-10">
          Enter your email address to receive password reset instructions via email.
        </p>

        <form className="space-y-6">
          <div className="space-y-2">
            <label className="block text-sm font-medium text-on-surface">Email Address</label>
            <input
              type="email"
              className="w-full bg-surface-container-lowest border border-outline-variant/30 rounded-xl px-4 py-3 text-on-surface placeholder:text-on-surface-variant focus:outline-none focus:ring-2 focus:ring-primary/20 focus:border-primary transition-all"
              placeholder="foreman@wms-ai.com"
              required
            />
          </div>
          <button
            type="button"
            className="w-full bg-gradient-to-br from-primary to-primary-container text-white rounded-xl px-4 py-3 font-medium transition-all hover:shadow-[0_20px_40px_-10px_rgba(53,37,205,0.4)] hover:scale-[1.01]"
          >
            Send Reset Link
          </button>
        </form>
      </div>
    </div>
  );
}
