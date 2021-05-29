export const Constants = {
    // The root URL of the API.
    API : '/api',

    // The sort directions.
    ORDER: {
        ASC: "asc",
        DESC: "desc"
    },

    // Default sort column.
    DEFAULT_SORT: "updatedOn",

    // Entities.
    ENTITY: {
        TRANSACTION: "transaction",
        TRANSACTION_CATEGORY: "transactionCategory",
        BILL: "bill",
        BILL_CATEGORY: "billCategory",
        ADDRESS: "address",
        AUTH: "auth",
        USER: "user",
        ELASTICSEARCH: "elasticsearch",
        NOTIFICATION: "notification"
    },

    // WebSocket
    WS: {
        // The base endpoint of the Websocket topics.
        ENDPOINT: "/notifications",
        // Websocket user broker.
        USER_BROKER: "/user/queue/specific-user"
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
    FONT_FAMILY: "Roboto-Regular",

    // Transaction category - Bills code
    BILLS_TRANSACTION_CATEGORY: "TC011",

    // Local storage items.
    LOCAL_STORAGE: {
        // Name of the Local storage key that keeps the user session info.
        SESSION: "finance-manager-session",
        // Name of the Local storage key that keeps the selected ui language.
        LANGUAGE: "finance-manager-language",
    },

    // Http headers.
    HEADERS: {
        AUTH : "authorization",
        AUTH_USER : "authorization-user"
    },
    
    // Images folder.
    IMAGES: '/images'
};