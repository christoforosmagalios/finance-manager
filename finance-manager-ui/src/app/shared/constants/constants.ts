export const Constants = {
    // The root URL of the API.
    API : '/api',

    // The sort directions.
    ORDER: {
        ASC: "asc",
        DESC: "desc"
    },

    // Entities.
    ENTITY: {
        TRANSACTION: "transaction",
        CATEGORY: "transactionCategory",
        BILL: "bill",
        BILL_CATEGORY: "billCategory"
    },

    // Items per page for tables.
    ITEMS_PER_PAGE: [5, 10, 20, 50, 100],

    // Languages.
    LANGUAGES: {
        EN: "en",
        EL: "el"
    },
    // Default Language.
    DEFAULT_LANGUAGE: "en",

    // Array of month translation keys.
    MONTHS: ["january", "february", "march", "april", "may", "june",
        "july", "august", "september", "october", "november", "december"
    ],

    // Default font family.
    FONT_FAMILY: "Nunito-Regular"
};