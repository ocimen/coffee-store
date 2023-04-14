### 1. Application Run

#### **Using Docker**

1) Make sure that your Docker client is running locally
2) Open your prompt in project folder and execute below code:

    ```sh
    docker build -t coffee-store .
    docker run -p 8080:8080 {image}
    ```
3) After the container run you can access the api from  <http://localhost:8080/>

### 2. Endpoints

**Coffee Store.postman_collection.json** file contains all requests that you can use and test api.

1) Product: Admin can use it for product crud operations
2) Topping: Admin can use it for topping crud operations
3) Cart: Customer can add / remove product and clear his/her cart
4) Order: Customer can create an order from his/her cart.
5) Admin: Admin can get most used toppings reports

### 3. Design Decisions and Improvement Points

1) Product, Topping and Admin controller needs to be under authentication but it's not implemented. Those endpoints are under **/api/admin** path.
2) Not every API response use DTO, some of them use Models. It's better to add related DTOs and mappers.
3) Get methods do not have paging support. It can be added later.
4) Add more Integration Tests
5) Add more Unit Test


