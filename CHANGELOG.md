# Changelog

All notable changes to this project will be documented in this file.

The format is based on [Keep a Changelog](https://keepachangelog.com/en/1.1.0/),
and this project adheres to [Calendar Versioning](https://calver.org/) of
the following form: YYYY.0M.0D.

## [2025.11.05]

### Added

- Designed abstract class [BudgetTrackerSecondary](src\BudgetTrackerSecondary.java) for [BudgetTracker](src\BudgetTracker.java) component

### Changed

- Moved [BudgetTrackerAlpha](src\Archive\BudgetTrackerAlpha.java) to Archive to keep main [src](src) folder with the main hirearchy of the components.

- Changed design to include toString(), hashCode(), and equals() inside the secondary abstract class.
- Added methods in the enhanced class such as getMonthlyIncome(), getCategories(), getBudgetLimit(String category), getCategoryExpense(String category),  getExpenseDates(String category), and getExpenseAmount(String category, String date) to help with the design of the secondary class.
- Changed name of BudgetTracker1L to [BudgetTrackerOnMap](src\BudgetTrackerOnMap.java) (To be revised to fit within scope of hirearchy) to serve as future Kernel Implementation class.

### Fixed

- N/A

### Removed

- N/A

## [2025.10.22]

### Added

- Added bodies to various method to allow full functionality upon call (To be tested).

### Changed

- Changed format of the entire code having a dedicated portion for methods and main.

### Fixed

- Fixed Long and int issues inside of multiple methods.

### Removed

- Removed all of the version 1 code with barebones methods and explanations for more detailed alternative.

## [2025.10.09]

### Added

- Completed [02-component-proof-of-concept](doc\02-component-proof-of-concept\02-component-proof-of-concept.md)
- Added "hasCategory" method to the kernerl
- Created "[BudgetTracker.java](src\BudgetTracker.java)" file
- Added kernel methods from the original design into the file as a proposal of types.
- Added some fields with possible representations of income, expenses, and savings.
- Added a dummy main method showing the component in action at an early prototype at using the income method.

### Changed

- Now able to track any categories of user desire.

### Fixed

- N/A

### Removed

- Removed constrain of budget tracker to just activities.

## [2025.09.19]

### Added

- Completed [Componet Brianstorming document](doc/01-component-brainstorming/01-component-brainstorming.md)

### Changed

- N/A

### Fixed

- N/A

### Removed

## [2025.10.25]

### Added

- Designed [kernel](src\BudgetTrackerKernel.java) and [enhanced interfaces](src\BudgetTracker.java) for [BudgetTrackerAlpha.java](src\BudgetTrackerAlpha.java) component.

- Created Version 1 of an implementation class for [BudgetTrackerAlpha.java](src\BudgetTrackerAlpha.java) named [BudgetTracker1L.java](src\BudgetTracker1L.java).

### Updated

- Changed design to include more descriptive javadoc comments to make the uses of each method more understandable.

- N/A

## [Unreleased]

### Added

- N/A

### Changed

- N/A

### Fixed

- N/A

### Removed

- N/A
