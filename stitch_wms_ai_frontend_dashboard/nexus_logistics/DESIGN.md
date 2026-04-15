# Design System Specification: The Architectural Intelligence (AI-WMS)

## 1. Overview & Creative North Star
**Creative North Star: "The Digital Foreman"**
In the world of high-velocity logistics and AI-driven warehousing, noise is the enemy. This design system moves away from the cluttered, "dashboard-heavy" aesthetics of legacy enterprise software. Instead, it adopts a philosophy of **High-Density Precision**. 

We treat the interface like a premium architectural blueprint: it is authoritative, sprawling, yet hyper-organized. We break the "template" look by utilizing **intentional asymmetry**—where data-heavy columns are balanced by expansive white space—and **tonal layering**, ensuring the UI feels like a sophisticated tool rather than a generic website.

---

## 2. Colors & Surface Philosophy
Our palette is rooted in a "Deep Indigo" foundation, utilizing Material-inspired tonal shifts to create depth without visual clutter.

### The "No-Line" Rule
**Borders are a design failure.** To maintain a premium, editorial feel, designers are prohibited from using 1px solid borders to section off content. Boundaries must be defined solely through background color shifts.
*   **Example:** A `surface-container-low` module sitting on a `surface` background provides all the definition needed.

### Surface Hierarchy & Nesting
Treat the UI as a series of physical layers—like stacked sheets of frosted glass.
*   **Base Layer:** `surface` (#faf8ff)
*   **Sectioning:** `surface-container-low` (#f2f3ff)
*   **Interactive Cards:** `surface-container-lowest` (#ffffff)
*   **Persistent Overlays:** `surface-container-high` (#e2e7ff)

### The "Glass & Gradient" Rule
To inject "soul" into the data, use Glassmorphism for floating elements (e.g., Command Palettes or Tooltips). Use a `backdrop-blur: 12px` with a semi-transparent `surface_variant`. 
*   **Signature Texture:** For primary CTAs, use a subtle linear gradient from `primary` (#3525cd) to `primary_container` (#4f46e5) at a 135° angle.

---

## 3. Typography: The Editorial Edge
We use **Inter** for its mathematical precision and readability at small scales.

*   **Display (lg/md):** Reserved for high-level warehouse metrics. Use `on_surface` with `-0.02em` letter spacing to feel "tight" and engineered.
*   **Headline (sm/md):** Use these to anchor large data sections. Pair a `headline-sm` with a `label-md` in `tertiary` color for a sophisticated, "tagged" look.
*   **Body & Labels:** In a high-density WMS, `body-sm` (0.75rem) is your workhorse. Ensure a line height of `1.5` to maintain legibility during rapid scanning.

---

## 4. Elevation & Depth
We eschew traditional "Drop Shadows" in favor of **Tonal Layering**.

*   **The Layering Principle:** Depth is achieved by "stacking." Place a `surface-container-lowest` card on a `surface-container-low` background. This creates a soft, natural lift that mimics fine stationery.
*   **Ambient Shadows:** When an element must float (e.g., a modal), use an ultra-diffused shadow: `box-shadow: 0 20px 40px -10px rgba(15, 23, 42, 0.08)`. The shadow color is a tint of our `on_surface` blue, never pure black.
*   **The Ghost Border Fallback:** If a border is required for accessibility, use the `outline_variant` at **15% opacity**. 100% opaque borders are strictly forbidden.

---

## 5. Components

### Minimalist Cards
*   **Styling:** No borders. `8px-12px` rounded corners. 
*   **Layout:** Use padding-heavy headers (24px) versus dense content areas (16px) to create an editorial rhythm.

### High-Density Data Tables
*   **The Rule:** Forbid divider lines between rows. Use a subtle `surface-container-low` background on hover.
*   **Cell Alignment:** Use `label-md` for headers in `on_surface_variant` (all caps, +0.05em tracking). Data cells use `body-md` for maximum legibility.

### Status Badges (The "Signals")
*   **Success/Warning/Danger:** Do not use heavy solid backgrounds. Use a 10% opacity fill of the status color with high-contrast text. This keeps the "data-driven" look clean and non-distracting.

### Collapsible Sidebar
*   **Styling:** Use `surface_dim` for the background to provide a clear anchor point. 
*   **Interaction:** Icons should transition from `on_surface_variant` to `primary` with a subtle `primary_fixed` glow on active states.

### Buttons
*   **Primary:** Gradient fill (`primary` to `primary_container`). `8px` radius.
*   **Tertiary (Ghost):** No background, no border. Only `on_surface` text. These are used for secondary actions to keep the focus on the primary workflow.

---

## 6. Do’s and Don’ts

### Do:
*   **Embrace Negative Space:** Let the AI insights breathe. Even in high-density views, use 32px or 48px margins between major functional blocks.
*   **Use Tonal Shifts:** Use `surface-container-highest` to draw the eye to a "Live Notification" or "Urgent Pick-list."
*   **Color as Information:** Only use `tertiary` (#7e3000) for AI-generated insights or "smart" recommendations to distinguish them from manual data.

### Don’t:
*   **Don't use 1px borders.** (If you think you need one, try a 4px gap or a background color shift instead).
*   **Don't use pure grey.** All "neutral" colors must be tinted with the `Deep Blue` palette to ensure the system feels cohesive and premium.
*   **Don't crowd the sidebar.** If the warehouse has 50 categories, use a "Search/Command" pattern rather than a never-ending scroll.

---
*Director's Final Note: This system is a scalpel, not a sledgehammer. Every pixel should feel like it was placed by an architect who understands the weight of global supply chains.*