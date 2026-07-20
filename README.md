# QRGenerator 🚀

A utility tool built in **Kotlin** designed to automate and simplify the generation of QR code images for testing and development environments. 

## 💡 The Problem
Previously, generating specific QR codes for testing required manually crafting `cURL` commands and using external web tools, which was slow, repetitive, and inefficient.

## 🛠️ The Solution
This tool allows developers and testers to generate custom QR codes dynamically by passing key parameters directly:
* **Amount** (Monto)
* **Usage Type** (Único uso o múltiples)
* **Details/Description** (Detalle del QR)
* **Expiration Time** (Tiempo de duración)

Once executed, the tool instantly generates and displays the requested QR code on screen, saving valuable time during QA cycles and debugging.

## ⚙️ CI/CD & Automation
The project is fully integrated with **GitHub Actions**. On every Pull Request (PR), the CI pipeline automatically builds and packages executable binaries for both **macOS** and **Windows**, ensuring seamless distribution and verification across different operating systems.

## 🛠️ Tech Stack
* **Language:** Kotlin (100%)
* **Build System:** Gradle (Kotlin DSL)
* **CI/CD:** GitHub Actions
