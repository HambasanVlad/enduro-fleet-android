# Enduro Fleet (Android, Kotlin + Jetpack Compose) - Lab 2: CRUD, UI only

Aplicație nativă Android (Kotlin + Jetpack Compose, Android 8.0+) pentru flota de motociclete enduro.
Datele sunt false, ținute **doar în memorie** (fără bază de date, fără server).

Entitatea **Motorcycle**: model, VIN, ore motor, ore de la ultimul schimb de ulei,
ore de la ultimul schimb de piston, probleme.

## Rulare

- **Pe telefon (fără nimic instalat pe laptop):** pe telefon deschide
  Releases → latest din acest repo, descarcă `EnduroFleet.apk` și instalează-l.
- **Pe laptop:** deschide folderul în **Android Studio** și apasă ▶ Run (pe emulator sau pe telefonul conectat prin USB).

La fiecare push, GitHub Actions (`.github/workflows/android-build.yml`) compilează aplicația și publică APK-ul.

## Structura

| Fișier | Rol |
|---|---|
| `app/src/main/java/.../MainActivity.kt` | Punctul de pornire, tema și navigarea între ecrane |
| `.../model/Motorcycle.kt` | Entitatea |
| `.../model/MotorcycleForm.kt` | Starea formularului + validări |
| `.../data/MotorcycleViewModel.kt` | Date false în memorie + add / update / delete |
| `.../ui/MotorcycleListScreen.kt` | **Read** (lista) + **Delete** (buton coș + confirmare) |
| `.../ui/AddMotorcycleScreen.kt` | **Create**: ecran separat |
| `.../ui/EditMotorcycleScreen.kt` | **Update**: ecran separat, câmpuri precompletate |
| `.../ui/MotorcycleFormFields.kt` | Câmpurile refolosite de Add și Edit |
| `.../ui/FormScreen.kt` | Scheletul comun Add/Edit (bara de sus + buton) |
