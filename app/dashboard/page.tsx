import { 
  TrendingUp, 
  PackageCheck, 
  AlertTriangle, 
  BarChart3,
  ArrowUpRight,
  BrainCircuit,
  Clock
} from "lucide-react";

export default function DashboardPage() {
  return (
    <div className="max-w-7xl mx-auto space-y-12 pb-12">
      {/* Header Section */}
      <div className="flex flex-col md:flex-row md:items-end justify-between gap-6">
        <div>
          <div className="flex items-center gap-2 text-tertiary mb-3">
            <BrainCircuit className="w-4 h-4" />
            <span className="text-sm font-semibold tracking-wide uppercase">AI Insight Active</span>
          </div>
          <h1 className="text-4xl text-on-surface font-semibold tracking-tight">
            System Overview
          </h1>
          <p className="text-on-surface-variant mt-2 text-sm max-w-2xl">
            Warehouse operations are currently running at 94% efficiency. Predictive models suggest increasing workforce in Sector C by 14:00 to handle incoming freight.
          </p>
        </div>
        
        <div className="flex items-center gap-3">
          <button className="px-5 py-2.5 rounded-xl border border-outline-variant/20 text-on-surface text-sm font-medium hover:bg-surface-container-low transition-colors">
            Generate Report
          </button>
          <button className="px-6 py-2.5 rounded-xl bg-gradient-to-br from-primary to-primary-container text-white text-sm font-medium hover:shadow-[0_20px_40px_-10px_rgba(53,37,205,0.4)] transition-all hover:scale-[1.02]">
            Optimize Routes
          </button>
        </div>
      </div>

      {/* High-Level Metrics (Minimalist Cards) */}
      <div className="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-4 gap-6">
        {[
          { label: "Total Shipments", value: "12,450", change: "+14.2%", icon: PackageCheck, status: "success" },
          { label: "Active Routes", value: "342", change: "+5.1%", icon: TrendingUp, status: "success" },
          { label: "Critical Delays", value: "8", change: "-2.4%", icon: AlertTriangle, status: "danger" },
          { label: "System Uptime", value: "99.9%", change: "Optimal", icon: BarChart3, status: "neutral" }
        ].map((metric, idx) => (
          <div 
            key={idx}
            className="bg-surface-container-lowest rounded-2xl p-6 shadow-[0_20px_40px_-10px_rgba(15,23,42,0.04)] flex flex-col justify-between group hover:-translate-y-1 transition-transform"
          >
            <div className="flex justify-between items-start mb-8">
              <span className="text-on-surface-variant text-sm font-medium">{metric.label}</span>
              <div className={`p-2 rounded-xl bg-surface-container-low text-on-surface-variant group-hover:text-primary transition-colors`}>
                <metric.icon className="w-5 h-5" />
              </div>
            </div>
            
            <div className="flex items-end justify-between">
              <span className="text-3xl font-semibold text-on-surface tracking-tight">{metric.value}</span>
              <span className={`text-xs font-medium px-2 py-1 rounded-full flex items-center gap-1
                ${metric.status === "success" ? "text-emerald-700 bg-emerald-500/10" : 
                  metric.status === "danger" ? "text-error bg-error/10" : 
                  "text-primary bg-primary/10"}`}
              >
                {metric.status !== "neutral" && <ArrowUpRight className="w-3 h-3" />}
                {metric.change}
              </span>
            </div>
          </div>
        ))}
      </div>

      <div className="grid grid-cols-1 lg:grid-cols-3 gap-8">
        {/* High-Density Data Table */}
        <div className="lg:col-span-2 bg-surface-container-lowest rounded-2xl p-6 shadow-[0_20px_40px_-10px_rgba(15,23,42,0.04)]">
          <div className="flex items-center justify-between mb-6">
            <h2 className="text-lg font-semibold text-on-surface">Active Fulfillment Ops</h2>
            <button className="text-primary text-sm font-medium hover:text-primary-container transition-colors">
              View All
            </button>
          </div>
          
          <div className="overflow-x-auto">
            <table className="w-full text-left border-collapse">
              <thead>
                <tr className="text-on-surface-variant text-xs uppercase tracking-wider border-b-0">
                  <th className="pb-4 font-medium">Order ID</th>
                  <th className="pb-4 font-medium">Destination</th>
                  <th className="pb-4 font-medium">ETA</th>
                  <th className="pb-4 font-medium">Status</th>
                  <th className="pb-4 text-right font-medium">Priority</th>
                </tr>
              </thead>
              <tbody className="text-sm">
                {[
                  { id: "ORD-8901", dest: "Seattle, WA", eta: "14:30 EST", status: "In Transit", priority: "High" },
                  { id: "ORD-8902", dest: "Austin, TX", eta: "16:00 EST", status: "Processing", priority: "Normal" },
                  { id: "ORD-8903", dest: "Miami, FL", eta: "Delayed", status: "Exception", priority: "Urgent" },
                  { id: "ORD-8904", dest: "Denver, CO", eta: "09:00 EST", status: "Delivered", priority: "Normal" },
                  { id: "ORD-8905", dest: "Boston, MA", eta: "11:15 EST", status: "In Transit", priority: "Low" },
                ].map((row, idx) => (
                  <tr key={idx} className="group hover:bg-surface-container-low transition-colors rounded-xl">
                    <td className="py-3 font-medium text-on-surface rounded-l-xl pl-2">{row.id}</td>
                    <td className="py-3 text-on-surface-variant">{row.dest}</td>
                    <td className="py-3 text-on-surface-variant">{row.eta}</td>
                    <td className="py-3">
                      <span className={`inline-flex px-2 py-0.5 rounded-full text-xs font-medium 
                        ${row.status === "In Transit" ? "bg-primary/10 text-primary" : 
                          row.status === "Exception" ? "bg-error/10 text-error" : 
                          row.status === "Delivered" ? "bg-emerald-500/10 text-emerald-700" :
                          "bg-surface-dim text-on-surface-variant"}`}
                      >
                        {row.status}
                      </span>
                    </td>
                    <td className="py-3 text-right rounded-r-xl pr-2">
                      <span className={`text-xs font-semibold ${row.priority === "Urgent" ? "text-error" : "text-on-surface"}`}>
                        {row.priority}
                      </span>
                    </td>
                  </tr>
                ))}
              </tbody>
            </table>
          </div>
        </div>

        {/* AI Tonal Layering Insight Card */}
        <div className="bg-surface-container-highest rounded-2xl p-8 flex flex-col justify-between shadow-[0_20px_40px_-10px_rgba(15,23,42,0.06)] relative overflow-hidden">
          {/* Subtle gradient overlay */}
          <div className="absolute inset-0 bg-gradient-to-br from-tertiary/5 to-transparent pointer-events-none" />
          
          <div className="relative z-10">
            <div className="flex items-center gap-2 mb-6 text-tertiary">
              <BrainCircuit className="w-5 h-5" />
              <h3 className="font-semibold text-sm uppercase tracking-wide">Predictive Insight</h3>
            </div>
            
            <p className="text-on-surface text-lg leading-relaxed font-medium mb-6">
              Sector <span className="text-tertiary font-bold">G</span> is showing pre-bottleneck patterns. Relocating 2 automated pickers from Sector B will prevent a projected 14-minute delay.
            </p>
            
            <div className="flex items-start gap-3 bg-surface/60 backdrop-blur-md rounded-xl p-4 border border-outline-variant/10">
              <Clock className="w-5 h-5 text-on-surface-variant shrink-0 mt-0.5" />
              <div>
                <p className="text-sm font-semibold text-on-surface">Time left to optimize</p>
                <p className="text-xs text-on-surface-variant">Action required in 22 mins.</p>
              </div>
            </div>
          </div>
          
          <div className="mt-8 relative z-10">
            <button className="w-full bg-white text-on-surface shadow-sm hover:shadow-md border border-outline-variant/20 py-3 rounded-xl font-medium transition-all hover:-translate-y-0.5 flex justify-center items-center gap-2">
              Execute Optimization
              <ArrowUpRight className="w-4 h-4 text-tertiary" />
            </button>
            <button className="w-full mt-3 text-on-surface-variant text-sm font-medium hover:text-on-surface transition-colors py-2">
              Dismiss
            </button>
          </div>
        </div>
      </div>
    </div>
  );
}
