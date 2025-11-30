-- Sofi --
CREATE TABLE instruktur_gym (
    id_instruktur     SERIAL PRIMARY KEY,
    kode_instruktur   VARCHAR(12) NOT NULL UNIQUE,
    nama              VARCHAR(100) NOT NULL,
    usia              INT CHECK (usia BETWEEN 16 AND 80),
    jenis_kelamin     VARCHAR(20) CHECK (jenis_kelamin IN ('Laki-laki', 'Perempuan')),
    no_hp             VARCHAR(20),
    alamat            TEXT
);

CREATE TABLE keahlian_gym (
    id_keahlian      SERIAL PRIMARY KEY,
    nama_keahlian    VARCHAR(100) NOT NULL UNIQUE
);

CREATE TABLE instruktur_keahlian (
    id_instruktur   INT NOT NULL,
    id_keahlian     INT NOT NULL,

    PRIMARY KEY (id_instruktur, id_keahlian),

    FOREIGN KEY (id_instruktur)
        REFERENCES instruktur_gym(id_instruktur)
        ON UPDATE CASCADE
        ON DELETE CASCADE,

    FOREIGN KEY (id_keahlian)
        REFERENCES keahlian_gym(id_keahlian)
        ON UPDATE CASCADE
        ON DELETE CASCADE
);

-- Iqo --
CREATE TABLE member_gym (
    id_member       SERIAL PRIMARY KEY,
    kode_member     VARCHAR(12) NOT NULL UNIQUE,
    nama            VARCHAR(100) NOT NULL,
    jenis_kelamin   VARCHAR(20) CHECK (jenis_kelamin IN ('Laki-laki', 'Perempuan')),
    usia            INT CHECK (usia BETWEEN 10 AND 90),
    no_hp           VARCHAR(20),
    alamat          TEXT,
    tanggal_daftar  DATE NOT NULL DEFAULT CURRENT_DATE
);

-- Fearent --
CREATE TABLE jadwal_kelas (
    id_kelas        SERIAL PRIMARY KEY,
    nama_kelas      VARCHAR(100) NOT NULL,
    hari            VARCHAR(20) CHECK (hari IN 
                    ('Senin','Selasa','Rabu','Kamis','Jumat','Sabtu','Minggu')),
    jam_kelas       TIME NOT NULL,
    id_instruktur   INT NOT NULL,

    FOREIGN KEY (id_instruktur)
        REFERENCES instruktur_gym(id_instruktur)
        ON UPDATE CASCADE
        ON DELETE RESTRICT
);

-- Ubay --
CREATE TABLE pendaftaran_kelas (
    id_pendaftaran  SERIAL PRIMARY KEY,
    id_member       INT NOT NULL,
    id_kelas        INT NOT NULL,
    tanggal_daftar  DATE NOT NULL DEFAULT CURRENT_DATE,
    catatan         TEXT,

    FOREIGN KEY (id_member)
        REFERENCES member_gym(id_member)
        ON UPDATE CASCADE
        ON DELETE CASCADE,

    FOREIGN KEY (id_kelas)
        REFERENCES jadwal_kelas(id_kelas)
        ON UPDATE CASCADE
        ON DELETE CASCADE
);
