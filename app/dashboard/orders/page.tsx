import { ShoppingCart } from "lucide-react";

export default function PurchaseOrdersPage() {
  return (
    <div className="max-w-7xl mx-auto space-y-8 pb-12">
      <div>
        <h1 className="text-4xl text-on-surface font-semibold tracking-tight">Purchase Orders</h1>
        <p className="text-on-surface-variant mt-2 text-sm max-w-2xl">
          Manage incoming freight and supplier purchase orders.
        </p>
      </div>

      <div className="bg-surface-container-lowest rounded-2xl shadow-sm border border-outline-variant/10 p-8 flex flex-col items-center justify-center h-96 text-center">
        <ShoppingCart className="w-16 h-16 text-outline-variant/50 mb-4" />
        <h3 className="text-lg font-medium text-on-surface mb-2">No Active Orders</h3>
        <p className="text-on-surface-variant text-sm max-w-sm mb-6">
          There are currently no inbound purchase orders scheduled. Create a new purchase order to alert receiving.
        </p>
        <button className="px-6 py-2.5 rounded-xl bg-gradient-to-br from-primary to-primary-container text-white text-sm font-medium hover:shadow-md transition-all">
          Create Purchase Order
        </button>
      </div>
    </div>
  );
}
