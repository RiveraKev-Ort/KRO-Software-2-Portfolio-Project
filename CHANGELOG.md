# Changelog

All notable changes to this project will be documented in this file.

The format is based on [Keep a Changelog](https://keepachangelog.com/en/1.1.0/),
and this project adheres to [Calendar Versioning](https://calver.org/) of
the following form: YYYY.0M.0D.

## [2025.12.10]

### Added

- Designed test suite for BudgetTrackerKernel and  BudgetTrackerSecondary components.

- Designed two different use cases for BudgetTrackerKernel and  BudgetTrackerSecondary component

### Updated

- Kernel Interface (BudgetTrackerKernel)

        Added read methods to support secondary logic without representation access:

        - getMonthlyIncome()
        - getBudgetLimit(String)
        - getCategories()
        - getExpenseDates(String)
        - getExpenseAmount(String, String)

        Declared constants: NO_LIMIT, DATE_FORMAT, DATE_LENGTH.

    Fully documented preconditions/postconditions for all methods.

- Enhanced Interface (BudgetTracker)

        Removed getter-style methods (moved to kernel for proper layering).
        Retained only secondary methods:

        - getAllExpensesSummary()
        - getBudgetSummary()
        - leftToBudget()

        Updated Javadoc to clarify formatting and contract requirements.

- Abstract Class (BudgetTrackerSecondary)

        - Implemented all enhanced methods using kernel + OSU Standard components (Sequence1L, Set1L, Map1L):

            - Sorted categories (A–Z) and dates (newest→oldest) via helper methods.
            - Added formatDollars() for currency formatting.


        - Added Object methods:

            - toString() → Compact summary using kernel values.
            - equals(Object) → Deep comparison of income, limits, and expenses.
            - hashCode() → Consistent with equals; uses named constants.

        - Enforced OSU style guide:

            Single return statements.
            No ternary operator.
            Braces on all control structures.
            Avoided magic numbers (introduced hashBase and hashPrime).
            Descriptive variable names and complete Javadoc.

- Refactored BudgetTracker1L to comply with OSU CSE Java Style Guide

        - Added complete Javadoc with @requires, @ensures, @updates, and @replaces for all public methods.

        - Enforced style rules:

            - No magic numbers → introduced constants (DATE_LENGTH, DATE_DASH_POS_1, etc.).
            - No ternary operators; single return per method.
            - Braces on all control statements; descriptive names.
            - Kernel purity maintained (no calls to other public component methods).

### Fixed

- N/A

### Removed

- N/A

## [2025.11.23]

### Added

- Designed kernel implementation for [BudgetTrackerKernel](src\BudgetTrackerKernel.java) component.
- Kernel implementation now fully implemented at [BudgetTrackerOnMap](src\BudgetTrackerOnMap.java).

### Updated

- Changed design to move prevoiusly implemented secondary methods inside of kernel implementation to the secondary class.

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
