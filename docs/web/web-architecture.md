# Common Web architectures
## Web 1.0
The first web apps used the following approaches:
* HTML always served from backend;
* Interaction via links (GET) or web forms (GET, POST);
* Each user action requires full page reload.

Example of interaction:
```mermaid
sequenceDiagram
    participant User as User (Web Browser)
    participant WebServer as Web Server
    participant WebStore as Web Store (Backend)
    participant ProductDatabase as Product Database
    participant ShoppingCart as Shopping Cart (Backend)

    %% Step 1: User accesses the starting page of the web store
    User->>WebServer: HTTP GET / (Request homepage)
    WebServer->>WebStore: Fetch homepage data
    WebStore->>WebServer: Return homepage HTML, CSS
    note right of WebServer: The response can optionally include JS. <br/> It could be used for additional functionality <br/> like form validation or dynamic content.
    WebServer-->>User: HTTP 200 OK, Homepage HTML, CSS

    %% Step 2: User enters product name in search box
    User->>WebServer: HTTP POST /search (Form submission: query=laptop)
    
    %% Server responds with updated HTML
    WebServer->>WebStore: Search for products with name "laptop"
    WebStore->>ProductDatabase: SQL Query: "SELECT * FROM products WHERE name LIKE '%laptop%'"
    ProductDatabase-->>WebStore: Return rows: [(1, "Laptop A", 1200), (2, "Laptop B", 900)]
    WebStore-->>WebServer: Return search results data
    WebServer->>WebServer: Generate HTML for product list (including search results)
    WebServer-->>User: HTTP 200 OK, Updated HTML with search results

    %% Step 3: User chooses one of the found products
    User->>WebServer: HTTP GET /product/1 (Request product details page for product ID 1)
    WebServer->>WebStore: Fetch details for product ID 1
    WebStore->>ProductDatabase: SQL Query: "SELECT * FROM products WHERE id = 1"
    ProductDatabase-->>WebStore: Return row: (1, "Laptop A", "High-performance laptop", 1200)
    WebStore-->>WebServer: Return product details data
    WebServer->>WebServer: Generate HTML for product details page
    WebServer-->>User: HTTP 200 OK, Updated HTML with product details

    %% Step 4: User clicks product link to see details
    %% This step is handled in the previous "HTTP GET /product/1" where product details were displayed

    %% Step 5: User adds product to shopping cart
    User->>WebServer: HTTP POST /cart (Form submission: productId=1&quantity=1)
    WebServer->>ShoppingCart: Add product to shopping cart (Add to session or database)
    ShoppingCart-->>WebServer: Confirmation of product added (HTML form response)
    WebServer->>WebStore: Fetch updated shopping cart data
    WebStore-->>WebServer: Return updated shopping cart information
    WebServer->>WebServer: Generate HTML with updated shopping cart information
    WebServer-->>User: HTTP 200 OK, Updated HTML with shopping cart status

    %% Optional: No JavaScript required
    %% The page reloads each time to update content, so JavaScript is optional for page updates.
    %% The browser displays updated HTML content after each form submission.
```

## Single Page App (SPA)
In this case:
* Only initial page load gets HTML from backend;
* All subsequent interactions are rendered locally on frontend (in browser) using JavaScript browser APIs;
* All interactions are performed by sending HTTP requests (GET, POST, PUT, DELETE, possibly others) using JavaScript APIs (XHR, fetch);
* History API is used to simulate page changes in browser history.

Example of interaction:
```mermaid
sequenceDiagram
    participant User as User (Web Browser)
    participant WebServer as Web Server
    participant WebStore as Web Store (Backend)
    participant ProductDatabase as Product Database
    participant ShoppingCart as Shopping Cart (Backend)

    %% Step 1: User accesses the starting page of the web store
    User->>WebServer: HTTP GET / (Request homepage)
    WebServer->>WebStore: Fetch homepage data
    WebStore->>WebServer: Return homepage HTML, CSS, JS
    WebServer-->>User: HTTP 200 OK, Homepage HTML, CSS, JS

    %% Step 2: User enters product name in search box
    User->>WebServer: HTTP POST /search (Body: {"query": "laptop"})
    
    %% Add note explaining GET could be used for search (Right of User)
    note right of User: Instead of POST, GET could be used in Step 2.<br/>GET is typically used for search operations where no data is modified on the server, <br/> making it ideal for retrieving data.
    
    WebServer->>WebStore: Search for products with name "laptop"
    WebStore->>ProductDatabase: SQL Query: "SELECT * FROM products WHERE name LIKE '%laptop%'"
    ProductDatabase-->>WebStore: Return rows: [(1, "Laptop A", 1200), (2, "Laptop B", 900)]
    WebStore->>WebServer: Format rows as JSON: [{"id": 1, "name": "Laptop A", "price": 1200}, {"id": 2, "name": "Laptop B", "price": 900}]
    WebServer-->>User: HTTP 200 OK, JSON results to render search results on page
    
    %% Self-Message: Browser uses JSON to render search results
    User->>User: Use JSON to render product list on page

    %% Step 3: User chooses one of the found products
    User->>WebServer: HTTP GET /product/1 (Request details for product ID 1)
    WebServer->>WebStore: Fetch details for product ID 1
    WebStore->>ProductDatabase: SQL Query: "SELECT * FROM products WHERE id = 1"
    ProductDatabase-->>WebStore: Return row: (1, "Laptop A", "High-performance laptop", 1200)
    WebStore->>WebServer: Format row as JSON: {"id": 1, "name": "Laptop A", "description": "High-performance laptop", "price": 1200}
    WebServer-->>User: HTTP 200 OK, JSON product details
    
    %% Self-Message: Browser uses JSON to render product details
    User->>User: Use JSON to render product details on page

    %% Step 4: User clicks product link to see details
    %% This step is handled in the previous "HTTP GET /product/1" where product details were displayed

    %% Step 5: User adds product to shopping cart
    User->>WebServer: HTTP POST /cart (Body: {"productId": 1, "quantity": 1})
    WebServer->>ShoppingCart: Add product to shopping cart (Add to session or database)
    ShoppingCart-->>WebServer: Confirm product added (JSON: {"cartId": 123, "items": [{"productId": 1, "quantity": 1}]})
    WebServer-->>User: HTTP 200 OK, Updated shopping cart status (JSON: {"message": "Product added to cart", "cartId": 123, "totalItems": 1})

    %% Self-Message: Browser uses JSON to update shopping cart status
    User->>User: Use JSON to update shopping cart status on page

```
