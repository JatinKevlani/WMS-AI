import { Save } from "lucide-react";

export default function SettingsPage() {
  return (
    <div className="max-w-4xl mx-auto space-y-8 pb-12">
      <div>
        <h1 className="text-4xl text-on-surface font-semibold tracking-tight">Admin Settings</h1>
        <p className="text-on-surface-variant mt-2 text-sm max-w-2xl">
          Configure WMS-AI parameters, notifications, and user access.
        </p>
      </div>

      <div className="bg-surface-container-lowest border border-outline-variant/10 rounded-2xl overflow-hidden">
        <div className="p-6 md:p-8 border-b border-outline-variant/10">
          <h2 className="text-lg font-semibold text-on-surface mb-6">Profile Settings</h2>
          <div className="grid grid-cols-1 md:grid-cols-2 gap-6">
            <div className="space-y-2">
              <label className="block text-sm font-medium text-on-surface">Full Name</label>
              <input type="text" defaultValue="Admin Director" className="w-full bg-surface-container-low border border-outline-variant/30 rounded-xl px-4 py-2.5 text-on-surface focus:outline-none focus:border-primary text-sm" />
            </div>
            <div className="space-y-2">
              <label className="block text-sm font-medium text-on-surface">Email Address</label>
              <input type="email" defaultValue="foreman@wms-ai.com" className="w-full bg-surface-container-low border border-outline-variant/30 rounded-xl px-4 py-2.5 text-on-surface focus:outline-none focus:border-primary text-sm" />
            </div>
          </div>
        </div>

        <div className="p-6 md:p-8 border-b border-outline-variant/10">
          <h2 className="text-lg font-semibold text-on-surface mb-6">System Preferences</h2>
          <div className="space-y-6">
            <div className="flex items-center justify-between">
              <div>
                <h4 className="font-medium text-on-surface text-sm">Automated Predictive Optimization</h4>
                <p className="text-xs text-on-surface-variant mt-1">Allow AI to alter picking routes on-the-fly.</p>
              </div>
              <label className="relative inline-flex items-center cursor-pointer">
                <input type="checkbox" className="sr-only peer" defaultChecked />
                <div className="w-11 h-6 bg-surface-container-high peer-focus:outline-none rounded-full peer peer-checked:after:translate-x-full peer-checked:after:border-white after:content-[''] after:absolute after:top-[2px] after:left-[2px] after:bg-white after:border-gray-300 after:border after:rounded-full after:h-5 after:w-5 after:transition-all peer-checked:bg-primary"></div>
              </label>
            </div>
            
            <div className="flex items-center justify-between">
              <div>
                <h4 className="font-medium text-on-surface text-sm">Critical Delay SMS Alerts</h4>
                <p className="text-xs text-on-surface-variant mt-1">Receive text messages for priority exceptions.</p>
              </div>
              <label className="relative inline-flex items-center cursor-pointer">
                <input type="checkbox" className="sr-only peer" defaultChecked />
                <div className="w-11 h-6 bg-surface-container-high peer-focus:outline-none rounded-full peer peer-checked:after:translate-x-full peer-checked:after:border-white after:content-[''] after:absolute after:top-[2px] after:left-[2px] after:bg-white after:border-gray-300 after:border after:rounded-full after:h-5 after:w-5 after:transition-all peer-checked:bg-primary"></div>
              </label>
            </div>
          </div>
        </div>
        
        <div className="p-6 md:p-8 bg-surface-container-low/30 flex justify-end">
          <button className="flex items-center gap-2 px-6 py-2.5 rounded-xl bg-primary text-white text-sm font-medium hover:bg-primary-container transition-colors shadow-sm">
            <Save className="w-4 h-4" />
            Save Changes
          </button>
        </div>
      </div>
    </div>
  );
}
