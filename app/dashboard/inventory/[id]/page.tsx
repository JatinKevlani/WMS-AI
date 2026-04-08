import Link from "next/link";
import { ArrowLeft, Package, MapPin, BarChart3 } from "lucide-react";

export default async function ProductDetailsPage({ params }: { params: { id: string } }) {
  const resolvedParams = await params;
  return (
    <div className="max-w-5xl mx-auto space-y-8 pb-12">
      <Link href="/dashboard/inventory" className="inline-flex items-center gap-2 text-sm text-on-surface-variant hover:text-primary transition-colors">
        <ArrowLeft className="w-4 h-4" />
        Back to Inventory
      </Link>
      
      <div className="bg-surface-container-lowest p-8 rounded-2xl shadow-sm border border-outline-variant/10 flex flex-col md:flex-row gap-8">
        <div className="w-32 h-32 bg-surface-container-high rounded-xl flex items-center justify-center shrink-0">
          <Package className="w-12 h-12 text-on-surface-variant" />
        </div>
        
        <div className="flex-1">
          <div className="flex justify-between items-start">
            <div>
              <h1 className="text-3xl font-semibold text-on-surface">Product Name Placeholder</h1>
              <p className="text-on-surface-variant mt-1">SKU: {resolvedParams.id}</p>
            </div>
            <span className="bg-emerald-500/10 text-emerald-700 px-3 py-1 rounded-full text-sm font-medium">In Stock</span>
          </div>
          
          <div className="grid grid-cols-2 md:grid-cols-4 gap-6 mt-8">
            <div>
              <p className="text-sm font-medium text-on-surface-variant mb-1">Available Quantity</p>
              <p className="text-2xl font-semibold text-on-surface">1,240</p>
            </div>
            <div>
              <p className="text-sm font-medium text-on-surface-variant mb-1">Allocated (Orders)</p>
              <p className="text-2xl font-semibold text-on-surface">315</p>
            </div>
            <div>
              <p className="text-sm font-medium text-on-surface-variant mb-1">Incoming</p>
              <p className="text-2xl font-semibold text-on-surface">500</p>
            </div>
            <div>
              <p className="text-sm font-medium text-on-surface-variant mb-1">Reorder Point</p>
              <p className="text-2xl font-semibold text-error text-opacity-80">200</p>
            </div>
          </div>
        </div>
      </div>
      
      <div className="grid grid-cols-1 md:grid-cols-2 gap-8">
        <div className="bg-surface-container-lowest p-6 rounded-2xl border border-outline-variant/10">
          <div className="flex items-center gap-2 mb-6">
            <MapPin className="w-5 h-5 text-on-surface-variant" />
            <h2 className="font-semibold text-on-surface">Storage Location</h2>
          </div>
          <div className="space-y-4">
            <div className="flex justify-between pb-3 border-b border-outline-variant/10">
              <span className="text-on-surface-variant placeholder">Primary Bin</span>
              <span className="text-on-surface font-medium">A-01-B22</span>
            </div>
            <div className="flex justify-between pb-3 border-b border-outline-variant/10">
              <span className="text-on-surface-variant">Zone</span>
              <span className="text-on-surface font-medium">Fast Moving Consumer Goods</span>
            </div>
            <div className="flex justify-between pb-3">
              <span className="text-on-surface-variant">Condition Requirements</span>
              <span className="text-on-surface font-medium">Standard</span>
            </div>
          </div>
        </div>

        <div className="bg-surface-container-lowest p-6 rounded-2xl border border-outline-variant/10">
          <div className="flex items-center gap-2 mb-6">
            <BarChart3 className="w-5 h-5 text-on-surface-variant" />
            <h2 className="font-semibold text-on-surface">Demand Forecast</h2>
          </div>
          <div className="h-40 flex items-center justify-center bg-surface-container-low rounded-xl">
             <p className="text-sm text-on-surface-variant">Demand graph visualization</p>
          </div>
        </div>
      </div>
    </div>
  );
}
