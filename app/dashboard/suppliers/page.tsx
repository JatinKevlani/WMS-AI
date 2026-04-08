import { Truck, ExternalLink, Mail, Phone } from "lucide-react";

export default function SuppliersPage() {
  return (
    <div className="max-w-7xl mx-auto space-y-8 pb-12">
      <div className="flex flex-col md:flex-row md:items-end justify-between gap-6">
        <div>
          <h1 className="text-4xl text-on-surface font-semibold tracking-tight">Suppliers</h1>
          <p className="text-on-surface-variant mt-2 text-sm max-w-2xl">
            Directory of active suppliers and logistical partners.
          </p>
        </div>
        <button className="px-5 py-2.5 rounded-xl bg-primary/10 text-primary hover:bg-primary/20 text-sm font-medium transition-colors">
          Add New Supplier
        </button>
      </div>

      <div className="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-3 gap-6">
        {[
          { name: "Apex Manufacturing", type: "Components", status: "Active", leadTime: "14 days", score: 98 },
          { name: "Global Shipments Inc.", type: "Logistics", status: "Active", leadTime: "3 days", score: 95 },
          { name: "Beta Packaging Co.", type: "Materials", status: "Under Review", leadTime: "24 days", score: 82 },
          { name: "XYZ Electronics", type: "Components", status: "Active", leadTime: "7 days", score: 99 },
        ].map((supplier, i) => (
          <div key={i} className="bg-surface-container-lowest border border-outline-variant/10 rounded-2xl p-6 hover:shadow-md transition-shadow group">
            <div className="flex justify-between items-start mb-4">
              <div className="w-12 h-12 bg-surface-container-low rounded-xl flex items-center justify-center">
                <Truck className="w-6 h-6 text-primary" />
              </div>
              <span className={`px-2 py-0.5 rounded-full text-xs font-semibold ${supplier.status === 'Active' ? 'bg-emerald-500/10 text-emerald-700' : 'bg-error/10 text-error'}`}>
                {supplier.status}
              </span>
            </div>
            <h3 className="text-lg font-semibold text-on-surface mb-1 group-hover:text-primary transition-colors">{supplier.name}</h3>
            <p className="text-sm text-on-surface-variant mb-6">{supplier.type}</p>
            
            <div className="flex justify-between border-t border-outline-variant/10 pt-4 mb-4">
              <div>
                <p className="text-xs text-on-surface-variant mb-1">Avg Lead Time</p>
                <p className="font-medium text-on-surface">{supplier.leadTime}</p>
              </div>
              <div className="text-right">
                <p className="text-xs text-on-surface-variant mb-1">Reliability Score</p>
                <p className={`font-medium ${supplier.score > 90 ? 'text-emerald-600' : 'text-error'}`}>{supplier.score}/100</p>
              </div>
            </div>

            <div className="flex gap-2">
              <button className="flex-1 p-2 rounded-lg bg-surface-container-low hover:bg-surface-dim text-on-surface-variant flex items-center justify-center transition-colors">
                <Mail className="w-4 h-4" />
              </button>
              <button className="flex-1 p-2 rounded-lg bg-surface-container-low hover:bg-surface-dim text-on-surface-variant flex items-center justify-center transition-colors">
                <Phone className="w-4 h-4" />
              </button>
              <button className="flex-1 p-2 rounded-lg bg-surface-container-low hover:bg-surface-dim text-on-surface-variant flex items-center justify-center transition-colors">
                <ExternalLink className="w-4 h-4" />
              </button>
            </div>
          </div>
        ))}
      </div>
    </div>
  );
}
