/**
 * WMS-AI API Client
 * Shared across all HTML pages for backend communication.
 */
const API_BASE = window.location.hostname === 'localhost' 
    ? 'http://localhost:8080/api' 
    : 'http://localhost:8080/api'; // Update with deployed backend URL

class WmsAPI {
    static getToken() {
        return localStorage.getItem('wms_token');
    }

    static getUser() {
        const user = localStorage.getItem('wms_user');
        return user ? JSON.parse(user) : null;
    }

    static isLoggedIn() {
        return !!this.getToken();
    }

    static logout() {
        localStorage.removeItem('wms_token');
        localStorage.removeItem('wms_user');
        window.location.href = '../login_page/code.html';
    }

    static requireAuth() {
        if (!this.isLoggedIn()) {
            window.location.href = '../login_page/code.html';
            return false;
        }
        return true;
    }

    static headers() {
        const h = { 'Content-Type': 'application/json' };
        const token = this.getToken();
        if (token) h['Authorization'] = `Bearer ${token}`;
        return h;
    }

    // ========== AUTH ==========
    static async login(email, password) {
        const res = await fetch(`${API_BASE}/auth/login`, {
            method: 'POST',
            headers: { 'Content-Type': 'application/json' },
            body: JSON.stringify({ email, password })
        });
        if (!res.ok) throw new Error('Invalid credentials');
        const data = await res.json();
        localStorage.setItem('wms_token', data.token);
        localStorage.setItem('wms_user', JSON.stringify({
            email: data.email,
            fullName: data.fullName,
            role: data.role
        }));
        return data;
    }

    static async register(email, password, fullName, role) {
        const res = await fetch(`${API_BASE}/auth/register`, {
            method: 'POST',
            headers: this.headers(),
            body: JSON.stringify({ email, password, fullName, role })
        });
        if (!res.ok) throw new Error((await res.json()).message || 'Registration failed');
        return await res.json();
    }

    // ========== PRODUCTS ==========
    static async getProducts(page = 0, size = 20) {
        const res = await fetch(`${API_BASE}/products?page=${page}&size=${size}`, { headers: this.headers() });
        if (!res.ok) throw new Error('Failed to fetch products');
        return await res.json();
    }

    static async getProduct(id) {
        const res = await fetch(`${API_BASE}/products/${id}`, { headers: this.headers() });
        if (!res.ok) throw new Error('Product not found');
        return await res.json();
    }

    static async searchProducts(query) {
        const res = await fetch(`${API_BASE}/products/search?q=${encodeURIComponent(query)}`, { headers: this.headers() });
        if (!res.ok) throw new Error('Search failed');
        return await res.json();
    }

    static async createProduct(product) {
        const res = await fetch(`${API_BASE}/products`, {
            method: 'POST', headers: this.headers(), body: JSON.stringify(product)
        });
        if (!res.ok) throw new Error((await res.json()).message || 'Failed to create product');
        return await res.json();
    }

    static async updateProduct(id, product) {
        const res = await fetch(`${API_BASE}/products/${id}`, {
            method: 'PUT', headers: this.headers(), body: JSON.stringify(product)
        });
        if (!res.ok) throw new Error('Failed to update product');
        return await res.json();
    }

    static async deleteProduct(id) {
        const res = await fetch(`${API_BASE}/products/${id}`, {
            method: 'DELETE', headers: this.headers()
        });
        if (!res.ok) throw new Error('Failed to delete product');
        return await res.json();
    }

    static async stockIn(id, quantity) {
        const res = await fetch(`${API_BASE}/products/${id}/stock-in`, {
            method: 'POST', headers: this.headers(), body: JSON.stringify({ quantity })
        });
        if (!res.ok) throw new Error('Stock-in failed');
        return await res.json();
    }

    static async stockOut(id, quantity) {
        const res = await fetch(`${API_BASE}/products/${id}/stock-out`, {
            method: 'POST', headers: this.headers(), body: JSON.stringify({ quantity })
        });
        if (!res.ok) throw new Error((await res.json()).message || 'Stock-out failed');
        return await res.json();
    }

    // ========== CATEGORIES ==========
    static async getCategories() {
        const res = await fetch(`${API_BASE}/categories`, { headers: this.headers() });
        if (!res.ok) throw new Error('Failed to fetch categories');
        return await res.json();
    }

    // ========== SUPPLIERS ==========
    static async getSuppliers() {
        const res = await fetch(`${API_BASE}/suppliers`, { headers: this.headers() });
        if (!res.ok) throw new Error('Failed to fetch suppliers');
        return await res.json();
    }

    static async getSupplier(id) {
        const res = await fetch(`${API_BASE}/suppliers/${id}`, { headers: this.headers() });
        if (!res.ok) throw new Error('Supplier not found');
        return await res.json();
    }

    static async createSupplier(supplier) {
        const res = await fetch(`${API_BASE}/suppliers`, {
            method: 'POST', headers: this.headers(), body: JSON.stringify(supplier)
        });
        if (!res.ok) throw new Error('Failed to create supplier');
        return await res.json();
    }

    // ========== SALES ==========
    static async getSales() {
        const res = await fetch(`${API_BASE}/sales`, { headers: this.headers() });
        if (!res.ok) throw new Error('Failed to fetch sales');
        return await res.json();
    }

    static async recordSale(productId, quantitySold, salePrice) {
        const res = await fetch(`${API_BASE}/sales`, {
            method: 'POST', headers: this.headers(),
            body: JSON.stringify({ productId, quantitySold, salePrice })
        });
        if (!res.ok) throw new Error((await res.json()).message || 'Sale failed');
        return await res.json();
    }

    // ========== ORDERS ==========
    static async getOrders() {
        const res = await fetch(`${API_BASE}/orders`, { headers: this.headers() });
        if (!res.ok) throw new Error('Failed to fetch orders');
        return await res.json();
    }

    static async createOrder(order) {
        const res = await fetch(`${API_BASE}/orders`, {
            method: 'POST', headers: this.headers(), body: JSON.stringify(order)
        });
        if (!res.ok) throw new Error('Failed to create order');
        return await res.json();
    }

    static async updateOrderStatus(id, status) {
        const res = await fetch(`${API_BASE}/orders/${id}/status`, {
            method: 'PUT', headers: this.headers(), body: JSON.stringify({ status })
        });
        if (!res.ok) throw new Error('Failed to update order status');
        return await res.json();
    }

    // ========== ALERTS ==========
    static async getAlerts() {
        const res = await fetch(`${API_BASE}/alerts`, { headers: this.headers() });
        if (!res.ok) throw new Error('Failed to fetch alerts');
        return await res.json();
    }

    static async getAlertCount() {
        const res = await fetch(`${API_BASE}/alerts/count`, { headers: this.headers() });
        if (!res.ok) return { count: 0 };
        return await res.json();
    }

    static async markAlertRead(id) {
        const res = await fetch(`${API_BASE}/alerts/${id}/read`, {
            method: 'PUT', headers: this.headers()
        });
        if (!res.ok) throw new Error('Failed to mark alert');
        return await res.json();
    }

    // ========== ANALYTICS ==========
    static async getDashboardStats() {
        const res = await fetch(`${API_BASE}/analytics/dashboard`, { headers: this.headers() });
        if (!res.ok) throw new Error('Failed to fetch stats');
        return await res.json();
    }

    static async getSalesAnalytics(period = 'MOM') {
        const res = await fetch(`${API_BASE}/analytics/sales?period=${period}`, { headers: this.headers() });
        if (!res.ok) throw new Error('Failed to fetch analytics');
        return await res.json();
    }

    // ========== AI RECOMMENDATIONS ==========
    static async getRestockRecommendation(productId) {
        const res = await fetch(`${API_BASE}/recommendations/restock/${productId}`, { headers: this.headers() });
        if (!res.ok) throw new Error('Failed to fetch recommendation');
        return await res.json();
    }

    static async getBundleRecommendation(productId) {
        const res = await fetch(`${API_BASE}/recommendations/bundle/${productId}`, { headers: this.headers() });
        if (!res.ok) throw new Error('Failed to fetch bundle recommendation');
        return await res.json();
    }

    static async getDemandForecast(productId) {
        const res = await fetch(`${API_BASE}/recommendations/forecast/${productId}`, { headers: this.headers() });
        if (!res.ok) throw new Error('Failed to fetch forecast');
        return await res.json();
    }

    // ========== FILE I/O ==========
    static async exportProducts() {
        const res = await fetch(`${API_BASE}/export/products`, { headers: this.headers() });
        if (!res.ok) throw new Error('Export failed');
        const blob = await res.blob();
        const url = URL.createObjectURL(blob);
        const a = document.createElement('a');
        a.href = url;
        a.download = 'products.csv';
        a.click();
        URL.revokeObjectURL(url);
    }

    static async exportSales() {
        const res = await fetch(`${API_BASE}/export/sales`, { headers: this.headers() });
        if (!res.ok) throw new Error('Export failed');
        const blob = await res.blob();
        const url = URL.createObjectURL(blob);
        const a = document.createElement('a');
        a.href = url;
        a.download = 'sales.csv';
        a.click();
        URL.revokeObjectURL(url);
    }

    // ========== SETTINGS ==========
    static async getSettings() {
        const res = await fetch(`${API_BASE}/settings`, { headers: this.headers() });
        if (!res.ok) return [];
        return await res.json();
    }

    static async updateSettings(settings) {
        const res = await fetch(`${API_BASE}/settings`, {
            method: 'PUT', headers: this.headers(), body: JSON.stringify(settings)
        });
        if (!res.ok) throw new Error('Failed to update settings');
        return await res.json();
    }

    // ========== UTILITY ==========
    static formatCurrency(amount) {
        return '₹' + Number(amount || 0).toLocaleString('en-IN', { minimumFractionDigits: 2 });
    }

    static formatDate(dateStr) {
        if (!dateStr) return 'N/A';
        return new Date(dateStr).toLocaleDateString('en-IN', {
            year: 'numeric', month: 'short', day: 'numeric'
        });
    }

    static showToast(message, type = 'success') {
        const toast = document.createElement('div');
        const bgColor = type === 'error' ? 'bg-red-500' : type === 'warning' ? 'bg-amber-500' : 'bg-green-500';
        toast.className = `fixed top-4 right-4 z-[9999] px-6 py-3 ${bgColor} text-white font-semibold rounded-xl shadow-2xl transform transition-all duration-300 translate-x-full`;
        toast.textContent = message;
        document.body.appendChild(toast);
        requestAnimationFrame(() => { toast.classList.remove('translate-x-full'); toast.classList.add('translate-x-0'); });
        setTimeout(() => {
            toast.classList.remove('translate-x-0');
            toast.classList.add('translate-x-full');
            setTimeout(() => toast.remove(), 300);
        }, 3000);
    }
}

// Auto-inject user info into sidebar if present
document.addEventListener('DOMContentLoaded', () => {
    const user = WmsAPI.getUser();
    if (user) {
        document.querySelectorAll('[data-user-name]').forEach(el => el.textContent = user.fullName);
        document.querySelectorAll('[data-user-email]').forEach(el => el.textContent = user.email);
        document.querySelectorAll('[data-user-role]').forEach(el => el.textContent = user.role);
    }
    
    // Global Header Actions
    Array.from(document.querySelectorAll('.material-symbols-outlined')).forEach(el => {
        const text = el.textContent.trim();
        const btn = el.closest('button') || el.closest('a') || el;
        if (text === 'print') {
            btn.addEventListener('click', (e) => { e.preventDefault(); window.print(); });
        } else if (text === 'ios_share' || text === 'file_upload' || text === 'cloud_upload' || text === 'download') {
            btn.addEventListener('click', (e) => { 
                // Ignore if it's explicitly a link to data_hub.html
                if (btn.tagName === 'A' && btn.href && btn.href.includes('data_hub')) return;
                e.preventDefault(); 
                WmsAPI.showToast('Export initiated...', 'info'); 
            });
        } else if (text === 'account_circle') {
            btn.addEventListener('click', (e) => { e.preventDefault(); WmsAPI.showToast(`User Profile: ${user?.fullName || 'Active User'}`, 'info'); });
        } else if (text === 'notifications') {
            btn.addEventListener('click', (e) => { e.preventDefault(); WmsAPI.showToast('No new notifications', 'info'); });
        }
    });
});

