import Link from "next/link";
import { Search, Filter, PackageOpen } from "lucide-react";

export default function InventoryPage() {
  return (
    <div className="max-w-7xl mx-auto space-y-8 pb-12">
      <div className="flex flex-col md:flex-row md:items-end justify-between gap-6">
        <div>
          <h1 className="text-4xl text-on-surface font-semibold tracking-tight">Product Master</h1>
          <p className="text-on-surface-variant mt-2 text-sm max-w-2xl">
            Complete inventory overview across all zones.
          </p>
        </div>
        <div className="flex items-center gap-3 w-full md:w-auto">
          <div className="relative flex-1 md:w-64">
            <Search className="w-4 h-4 absolute left-3 top-1/2 -translate-y-1/2 text-on-surface-variant" />
            <input type="text" placeholder="Search SKU or Name..." className="w-full pl-9 pr-4 py-2 bg-surface-container-low border border-outline-variant/30 rounded-xl text-sm focus:outline-none focus:border-primary" />
          </div>
          <button className="p-2 border border-outline-variant/30 rounded-xl bg-surface hover:bg-surface-container-low transition-colors text-on-surface-variant">
            <Filter className="w-5 h-5" />
          </button>
        </div>
      </div>

      <div className="bg-surface-container-lowest rounded-2xl shadow-[0_20px_40px_-10px_rgba(15,23,42,0.04)] border border-outline-variant/10 overflow-hidden">
        <table className="w-full text-left border-collapse">
          <thead className="bg-surface-container-low/50">
            <tr className="text-on-surface-variant text-xs uppercase tracking-wider">
              <th className="p-4 font-medium">SKU</th>
              <th className="p-4 font-medium">Product Name</th>
              <th className="p-4 font-medium">Category</th>
              <th className="p-4 font-medium text-right">Stock</th>
              <th className="p-4 font-medium text-right">Zone</th>
              <th className="p-4"></th>
            </tr>
          </thead>
          <tbody className="text-sm">
            {[
              { sku: "ELC-102", name: "Wireless Earbuds A2", cat: "Electronics", stock: 1240, zone: "A-01" },
              { sku: "HMG-881", name: "Ceramic Coffee Maker", cat: "Home Goods", stock: 85, zone: "B-12" },
              { sku: "MED-550", name: "Vitamin D3 Supplements", cat: "Health", stock: 3400, zone: "C-05" },
              { sku: "OFC-229", name: "Ergonomic Mesh Chair", cat: "Furniture", stock: 12, zone: "D-99" },
            ].map((product, i) => (
              <tr key={i} className="border-t border-outline-variant/10 hover:bg-surface-container-low transition-colors group">
                <td className="p-4 font-medium text-on-surface">{product.sku}</td>
                <td className="p-4 text-on-surface-variant">{product.name}</td>
                <td className="p-4"><span className="bg-surface-container-high px-2 py-1 rounded-md text-xs text-on-surface-variant">{product.cat}</span></td>
                <td className="p-4 text-right font-medium text-on-surface">{product.stock}</td>
                <td className="p-4 text-right text-on-surface-variant">{product.zone}</td>
                <td className="p-4 text-right">
                  <Link href={`/dashboard/inventory/${product.sku}`} className="text-primary hover:text-primary-container text-sm font-medium">
                    Details
                  </Link>
                </td>
              </tr>
            ))}
          </tbody>
        </table>
      </div>
    </div>
  );
}
