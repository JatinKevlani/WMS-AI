import { TrendingUp, Download } from "lucide-react";

export default function SalesTransactionsPage() {
  return (
    <div className="max-w-7xl mx-auto space-y-8 pb-12">
      <div className="flex items-end justify-between">
        <div>
          <h1 className="text-4xl text-on-surface font-semibold tracking-tight">Sales Transactions</h1>
          <p className="text-on-surface-variant mt-2 text-sm">
            Outbound fulfillment history and transactional value.
          </p>
        </div>
        <button className="flex items-center gap-2 px-4 py-2 border border-outline-variant/30 rounded-xl bg-surface hover:bg-surface-container-low text-on-surface-variant text-sm font-medium transition-colors">
          <Download className="w-4 h-4" /> Export
        </button>
      </div>

      <div className="bg-surface-container-lowest rounded-2xl shadow-sm border border-outline-variant/10 overflow-hidden">
        <table className="w-full text-left border-collapse">
          <thead className="bg-surface-container-low/50">
            <tr className="text-on-surface-variant text-xs uppercase tracking-wider">
              <th className="p-4 font-medium">Transaction ID</th>
              <th className="p-4 font-medium">Date</th>
              <th className="p-4 font-medium">Customer</th>
              <th className="p-4 font-medium">Total Items</th>
              <th className="p-4 font-medium text-right">Value</th>
            </tr>
          </thead>
          <tbody className="text-sm">
            {[
              { id: "TXN-9021", date: "Oct 24, 2023", cust: "Acme Logistics", items: 45, val: "$4,520.00" },
              { id: "TXN-9022", date: "Oct 24, 2023", cust: "Global Trade Inc", items: 12, val: "$1,100.50" },
              { id: "TXN-9023", date: "Oct 23, 2023", cust: "Stark Industries", items: 89, val: "$12,450.00" },
            ].map((txn, i) => (
              <tr key={i} className="border-t border-outline-variant/10 hover:bg-surface-container-low transition-colors">
                <td className="p-4 font-medium text-primary">{txn.id}</td>
                <td className="p-4 text-on-surface-variant">{txn.date}</td>
                <td className="p-4 text-on-surface">{txn.cust}</td>
                <td className="p-4 text-on-surface-variant">{txn.items} units</td>
                <td className="p-4 text-right font-medium text-on-surface">{txn.val}</td>
              </tr>
            ))}
          </tbody>
        </table>
      </div>
    </div>
  );
}
