# Payment Management Service

## Overview
This Payment Management Service is designed to handle order processing and payments securely and efficiently. It supports bulk uploads of order and payment data, data encryption, and the ability to process multiple payments for a single order. 

The project uses **Spring Boot**, **MySQL**, **Spring Batch**, and **AES encryption** to ensure data confidentiality and secure payment processing.

## Features
- **AES Data Encryption**: Secure storage of sensitive order and payment information.
- **Bulk Data Upload**: Efficient uploading and downloading of order and payment data using Spring Batch.
- **Multiple Payment Handling**: Allows multiple payments for a single order, keeping the order status pending until successful payment.
- **Concurrent Processing**: Ensures scalability and efficiency in handling bulk transactions.

## 1. Data Storage & Security

- **AES Encryption**: The application uses AES encryption to securely store sensitive information, such as payment details. This ensures that the data at rest in the database is encrypted and only decrypted during processing.
  
- **Table Structure**:
  - **Order Table**: Contains information like `order_id`, `order_amount`, `order_status`, and `created_at`.
  - **Payment Table**: Contains `payment_id`, `order_id`, `payment_amount`, `payment_status`, and `payment_method`.
  - **Order-Payment Relationship**: Each order can have multiple payments, and the `payment_status` is updated until a successful payment is received.

## 2. Uploading Order and Payment Data

- **Bulk Upload Process**: 
  - The system allows bulk uploading of order and payment data in CSV or XML format.
  - Use the `/upload` endpoint to upload files containing order and payment information.
  - The upload functionality is implemented using **Spring Batch**, which handles large data sets efficiently by breaking them into chunks and processing them asynchronously.

## 3. Spring Batch & Bulk Operations

- **Spring Batch**: The application leverages Spring Batch to process the bulk upload and download of order and payment data. It ensures high-performance processing by:
  - Splitting the data into manageable chunks.
  - Processing the data asynchronously in parallel, enhancing scalability.
  
- **File Upload**: 
  - Order and payment data files can be uploaded via an API, which triggers Spring Batch jobs to read and store the data in the database.

- **File Download**:
  - Similarly, bulk downloads are supported, allowing users to retrieve large amounts of order and payment data in the desired format.

## 4. Order and Payment Processing

- **Multiple Payment Handling**:
  - An order can have multiple payments associated with it. The system allows partial payments until the full order amount is received.
  - The order status is marked as `pending` during the payment process. Once a payment is successful, the status is updated to `completed`.

- **Processing Flow**:
  - When a payment is made, it is stored in the **Payment Table** with a `pending` status.
  - If a payment fails, the system allows another attempt until the payment is successful.
  - The order remains in the `pending` state until the total payment is processed successfully. This allows flexibility for customers to retry payments if they fail.

## How to Run

1. **Prerequisites**:  
   - Java (version X.X)  
   - MySQL  
   - Gradle

2. **Build**:  
   Run the following command to build the project:  
   ```bash
   ./gradlew build
