"use client";

import Link from "next/link";
import { usePathname } from "next/navigation";
import { 
  Box, 
  LayoutDashboard, 
  Package, 
  ShoppingCart, 
  Settings, 
  Truck,
  Bell,
  Search,
  Menu,
  BarChart2,
  BrainCircuit,
  TrendingUp
} from "lucide-react";

export default function DashboardLayout({
  children,
}: {
  children: React.ReactNode;
}) {
  const pathname = usePathname();

  const navigation = [
    { icon: LayoutDashboard, label: "Dashboard", href: "/dashboard" },
    { icon: BarChart2, label: "Analytics", href: "/dashboard/analytics" },
    { icon: BrainCircuit, label: "AI Recommendations", href: "/dashboard/recommendations" },
    { icon: Package, label: "Inventory", href: "/dashboard/inventory" },
    { icon: ShoppingCart, label: "Purchase Orders", href: "/dashboard/orders" },
    { icon: TrendingUp, label: "Sales Transactions", href: "/dashboard/sales" },
    { icon: Truck, label: "Suppliers", href: "/dashboard/suppliers" },
    { icon: Settings, label: "Settings", href: "/dashboard/settings" },
  ];

  return (
    <div className="min-h-screen flex w-full bg-surface">
      {/* Sidebar - Collapsible conceptual */}
      <aside className="w-20 lg:w-64 flex-shrink-0 bg-surface-dim flex flex-col justify-between transition-all duration-300 relative z-20 overflow-y-auto border-r border-outline-variant/10">
        <div>
          {/* Logo */}
          <div className="h-16 flex items-center justify-center lg:justify-start lg:px-6 sticky top-0 bg-surface-dim z-10">
            <Link href="/dashboard" className="flex items-center gap-3">
              <div className="bg-primary/10 p-2 rounded-lg">
                <Box className="w-6 h-6 text-primary" />
              </div>
              <span className="hidden lg:block font-bold text-on-surface tracking-wide">
                WMS-AI
              </span>
            </Link>
          </div>

          {/* Navigation */}
          <nav className="mt-6 flex flex-col gap-2 px-3 pb-6">
            {navigation.map((item, idx) => {
              const isActive = pathname === item.href;
              return (
                <Link
                  key={idx}
                  href={item.href}
                  className={`flex items-center gap-3 px-3 py-3 rounded-xl transition-all group relative overflow-hidden ${
                    isActive 
                      ? "text-primary bg-primary/5" 
                      : "text-on-surface-variant hover:text-primary hover:bg-primary/5"
                  }`}
                >
                  {isActive && (
                    <div className="absolute left-0 top-1/2 -translate-y-1/2 w-1 h-8 rounded-r-full bg-primary" />
                  )}
                  <item.icon className={`w-5 h-5 shrink-0 transition-colors ${isActive ? "text-primary shadow-[0_0_12px_rgba(53,37,205,0.3)]" : ""}`} />
                  <span className={`hidden lg:block text-sm font-medium ${isActive ? "text-primary" : ""}`}>{item.label}</span>
                </Link>
              );
            })}
          </nav>
        </div>

        {/* User Profile */}
        <div className="p-4 mb-4 sticky bottom-0 bg-surface-dim">
          <div className="flex items-center justify-center lg:justify-start gap-3 px-2">
            <img 
              src="https://api.dicebear.com/7.x/avataaars/svg?seed=Felix" 
              alt="User" 
              className="w-10 h-10 rounded-full bg-surface-container-highest border border-outline-variant/20 shrink-0" 
            />
            <div className="hidden lg:block overflow-hidden">
              <p className="text-sm font-medium text-on-surface truncate">Admin Director</p>
              <p className="text-xs text-on-surface-variant truncate">foreman@wms-ai.com</p>
            </div>
          </div>
        </div>
      </aside>

      {/* Main Content Area */}
      <main className="flex-1 flex flex-col min-w-0 bg-surface h-screen overflow-hidden">
        {/* Top Header */}
        <header className="h-16 bg-surface/80 backdrop-blur-md sticky top-0 z-10 flex items-center justify-between px-8 shrink-0">
          <div className="flex items-center gap-4 flex-1">
            <button className="lg:hidden text-on-surface-variant">
              <Menu className="w-5 h-5" />
            </button>
            
            {/* Search - Glassmorphism pattern */}
            <div className="hidden md:flex items-center gap-2 bg-surface-container-low px-4 py-2 rounded-full flex-1 max-w-md focus-within:ring-2 focus-within:ring-primary/20 transition-all">
              <Search className="w-4 h-4 text-on-surface-variant" />
              <input 
                type="text" 
                placeholder="Search orders, invoices, or products..." 
                className="bg-transparent border-none outline-none text-sm w-full text-on-surface placeholder:text-on-surface-variant"
              />
              <div className="flex items-center gap-1 text-[10px] font-semibold text-on-surface-variant bg-surface px-2 py-0.5 rounded-full">
                ⌘ K
              </div>
            </div>
          </div>

          <div className="flex items-center gap-4">
            <button className="relative text-on-surface-variant hover:text-on-surface transition-colors">
              <Bell className="w-5 h-5" />
              <span className="absolute top-0 right-0 w-2 h-2 bg-error rounded-full ring-2 ring-surface"></span>
            </button>
          </div>
        </header>

        {/* Page Content */}
        <div className="flex-1 overflow-y-auto p-4 md:p-8">
          {children}
        </div>
      </main>
    </div>
  );
}
