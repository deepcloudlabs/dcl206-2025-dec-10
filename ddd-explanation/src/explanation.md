# Domain and Sub-domain Structure

## Domain → Sub-domain

- **Domain → Sub-domain**
  - Analysis → Domain Expert
  - Core Sub-domain
  - Supporting Sub-domain
  - Generic Sub-domain

---

## Core-subdomain → Bounded Context (BC)

- **Core-subdomain → Bounded Context (BC)**  
  - Ubiquitous Language  
  - Domain Model  

- **Analysis → Design**
  - Hexagonal Architecture

---

## Sub-domain to Bounded Context Mapping

- **Sub-domain → Bounded Context (BC)**
  - `Sub-domain -- 1:1 --> BC`
  - `Sub-domain -- 1:N --> BC`

---

## Example: JTLS (Joint Theatre Level Simulation)

### Logistics Sub-domain

- **Logistics -- 1:N --> Bounded Contexts**
  - `Logistics-Operations (BC) -- 1:1 -> Java Module`
  - `Logistics-Reporting (BC)  -- 1:1 -> Java Module`

### Combat Sub-domain

- **Combat -- 1:N --> Bounded Contexts (modular monolith style)**
  - `Combat-Resolution (BC)  -- 1:1 -> Java Module`
  - `Combat-Calibration (BC) -- 1:1 -> Java Module`

- **Combat -- 1:N --> Bounded Contexts (microservice style)**
  - `Combat-Resolution (BC)  -- 1:N -> MicroService`
