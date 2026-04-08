import { BarChart3, TrendingUp, TrendingDown, Maximize2 } from "lucide-react";

export default function AnalyticsDashboard() {
  return (
    <div className="max-w-7xl mx-auto space-y-12 pb-12">
      <div className="flex flex-col md:flex-row md:items-end justify-between gap-6">
        <div>
          <h1 className="text-4xl text-on-surface font-semibold tracking-tight">Analytics Dashboard</h1>
          <p className="text-on-surface-variant mt-2 text-sm max-w-2xl">
            Deep dive into warehouse performance, storage density, and overall logistical throughput.
          </p>
        </div>
        <div className="flex items-center gap-3">
          <select className="px-4 py-2 border border-outline-variant/30 rounded-xl bg-surface text-on-surface text-sm">
            <option>Last 7 Days</option>
            <option>Last 30 Days</option>
            <option>Year to Date</option>
          </select>
          <button className="px-5 py-2.5 rounded-xl border border-outline-variant/20 text-on-surface text-sm font-medium hover:bg-surface-container-low transition-colors">
            Export CSV
          </button>
        </div>
      </div>

      <div className="grid grid-cols-1 md:grid-cols-3 gap-6">
        {[
          { label: "Storage Density", value: "88.4%", change: "+1.2%", trend: "up" },
          { label: "Average Picking Time", value: "2.4m", change: "-0.3m", trend: "down" },
          { label: "Order Accuracy", value: "99.98%", change: "+0.01%", trend: "up" },
        ].map((stat, i) => (
          <div key={i} className="bg-surface-container-lowest border border-outline-variant/10 rounded-2xl p-6 shadow-sm">
            <p className="text-sm font-medium text-on-surface-variant mb-4">{stat.label}</p>
            <div className="flex items-end justify-between">
              <span className="text-3xl font-semibold text-on-surface">{stat.value}</span>
              <span className={`flex items-center gap-1 text-sm ${stat.trend === "up" ? "text-emerald-600" : "text-primary"}`}>
                {stat.trend === "up" ? <TrendingUp className="w-4 h-4" /> : <TrendingDown className="w-4 h-4" />}
                {stat.change}
              </span>
            </div>
          </div>
        ))}
      </div>

      <div className="grid grid-cols-1 lg:grid-cols-2 gap-8">
        <div className="bg-surface-container-lowest border border-outline-variant/10 rounded-2xl p-6 h-[400px] flex flex-col items-center justify-center relative">
          <Maximize2 className="absolute top-6 right-6 w-5 h-5 text-on-surface-variant hover:text-on-surface cursor-pointer" />
          <BarChart3 className="w-16 h-16 text-outline-variant/50 mb-4" />
          <p className="text-on-surface-variant font-medium">Throughput Visualization</p>
          <p className="text-sm text-outline-variant">Interactive chart placeholder</p>
        </div>
        
        <div className="bg-surface-container-lowest border border-outline-variant/10 rounded-2xl p-6 h-[400px] flex flex-col items-center justify-center relative">
          <Maximize2 className="absolute top-6 right-6 w-5 h-5 text-on-surface-variant hover:text-on-surface cursor-pointer" />
          <BarChart3 className="w-16 h-16 text-outline-variant/50 mb-4" />
          <p className="text-on-surface-variant font-medium">Storage Capacity Map</p>
          <p className="text-sm text-outline-variant">Interactive map placeholder</p>
        </div>
      </div>
    </div>
  );
}
