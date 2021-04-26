CREATE TABLE car
(
    id           UUID         NOT NULL,

    make         VARCHAR(128) NOT NULL,
    model        VARCHAR(128),

    issue_year   integer      NOT NULL
        CONSTRAINT year_range CHECK (issue_year >= 1980 AND issue_year <= 2019),
    issue_month  integer     NOT NULL
        CONSTRAINT month_range CHECK (issue_month >= 1 AND issue_month <= 12),
    volume       integer     NOT NULL,
    capacity     integer     NOT NULL,

    turbocharger BOOLEAN      NOT NULL,
    transmission VARCHAR(128) NOT NULL,
    drive        VARCHAR(128) NOT NULL,
    body         VARCHAR(128) NOT NULL,
    color        VARCHAR(128) NOT NULL,

    deleted      BOOLEAN      NOT NULL,
    created_at   TIMESTAMP    NOT NULL,
    updated_at   TIMESTAMP    NOT NULL,


    CONSTRAINT pk_car_id PRIMARY KEY (id)
);
