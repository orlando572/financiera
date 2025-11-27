--
-- PostgreSQL database dump
--

\restrict mT1IhUEP7WjM3iCKiFMmCtGL5Ha1JkrvqOv822Vfc9K66IRdixF1yu59ldp05jW

-- Dumped from database version 18.1
-- Dumped by pg_dump version 18.0

-- Started on 2025-11-27 04:34:17

SET statement_timeout = 0;
SET lock_timeout = 0;
SET idle_in_transaction_session_timeout = 0;
SET transaction_timeout = 0;
SET client_encoding = 'UTF8';
SET standard_conforming_strings = on;
SELECT pg_catalog.set_config('search_path', '', false);
SET check_function_bodies = false;
SET xmloption = content;
SET client_min_messages = warning;
SET row_security = off;

--
-- TOC entry 5 (class 2615 OID 17740)
-- Name: public; Type: SCHEMA; Schema: -; Owner: -
--

-- *not* creating schema, since initdb creates it


--
-- TOC entry 4682 (class 0 OID 0)
-- Dependencies: 5
-- Name: SCHEMA public; Type: COMMENT; Schema: -; Owner: -
--

COMMENT ON SCHEMA public IS '';


SET default_tablespace = '';

SET default_table_access_method = heap;

--
-- TOC entry 219 (class 1259 OID 17741)
-- Name: administrador; Type: TABLE; Schema: public; Owner: -
--

CREATE TABLE public.administrador (
    id_admin integer NOT NULL,
    id_usuario integer NOT NULL,
    rol_detalle character varying(50),
    permisos_sistema jsonb,
    estado character varying(20) NOT NULL
);


--
-- TOC entry 220 (class 1259 OID 17749)
-- Name: administrador_id_admin_seq; Type: SEQUENCE; Schema: public; Owner: -
--

CREATE SEQUENCE public.administrador_id_admin_seq
    AS integer
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


--
-- TOC entry 4683 (class 0 OID 0)
-- Dependencies: 220
-- Name: administrador_id_admin_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: -
--

ALTER SEQUENCE public.administrador_id_admin_seq OWNED BY public.administrador.id_admin;


--
-- TOC entry 221 (class 1259 OID 17750)
-- Name: afp; Type: TABLE; Schema: public; Owner: -
--

CREATE TABLE public.afp (
    id_afp integer NOT NULL,
    id_institucion integer NOT NULL,
    nombre character varying(150) NOT NULL,
    codigo_sbs character varying(20),
    comision_flujo numeric(5,2),
    comision_saldo numeric(5,2),
    comision_mixta numeric(5,2),
    rentabilidad_promedio numeric(5,2),
    fondos_disponibles jsonb,
    estado character varying(20) NOT NULL
);


--
-- TOC entry 222 (class 1259 OID 17759)
-- Name: afp_id_afp_seq; Type: SEQUENCE; Schema: public; Owner: -
--

CREATE SEQUENCE public.afp_id_afp_seq
    AS integer
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


--
-- TOC entry 4684 (class 0 OID 0)
-- Dependencies: 222
-- Name: afp_id_afp_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: -
--

ALTER SEQUENCE public.afp_id_afp_seq OWNED BY public.afp.id_afp;


--
-- TOC entry 223 (class 1259 OID 17760)
-- Name: aporte_pension; Type: TABLE; Schema: public; Owner: -
--

CREATE TABLE public.aporte_pension (
    id_aporte integer NOT NULL,
    id_usuario integer NOT NULL,
    id_institucion integer NOT NULL,
    id_tipo_fondo integer NOT NULL,
    cuspp character varying(50),
    periodo character varying(10),
    monto_aporte numeric(12,2),
    aporte_trabajador numeric(12,2),
    aporte_empleador numeric(12,2),
    comision_cobrada numeric(12,2),
    seguro_invalidez numeric(12,2),
    fecha_aporte timestamp without time zone,
    empleador character varying(150),
    ruc_empleador character varying(20),
    salario_declarado numeric(10,2),
    dias_trabajados integer,
    observaciones text,
    estado character varying(20) NOT NULL
);


--
-- TOC entry 224 (class 1259 OID 17770)
-- Name: aporte_pension_id_aporte_seq; Type: SEQUENCE; Schema: public; Owner: -
--

CREATE SEQUENCE public.aporte_pension_id_aporte_seq
    AS integer
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


--
-- TOC entry 4685 (class 0 OID 0)
-- Dependencies: 224
-- Name: aporte_pension_id_aporte_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: -
--

ALTER SEQUENCE public.aporte_pension_id_aporte_seq OWNED BY public.aporte_pension.id_aporte;


--
-- TOC entry 225 (class 1259 OID 17771)
-- Name: asesor; Type: TABLE; Schema: public; Owner: -
--

CREATE TABLE public.asesor (
    id_asesor integer NOT NULL,
    id_usuario integer NOT NULL,
    especialidad character varying(50),
    codigo_asesor character varying(50) NOT NULL,
    estado character varying(20) NOT NULL
);


--
-- TOC entry 226 (class 1259 OID 17778)
-- Name: asesor_id_asesor_seq; Type: SEQUENCE; Schema: public; Owner: -
--

CREATE SEQUENCE public.asesor_id_asesor_seq
    AS integer
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


--
-- TOC entry 4686 (class 0 OID 0)
-- Dependencies: 226
-- Name: asesor_id_asesor_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: -
--

ALTER SEQUENCE public.asesor_id_asesor_seq OWNED BY public.asesor.id_asesor;


--
-- TOC entry 227 (class 1259 OID 17779)
-- Name: asesoramiento_financiero; Type: TABLE; Schema: public; Owner: -
--

CREATE TABLE public.asesoramiento_financiero (
    id_asesoramiento integer NOT NULL,
    id_usuario integer NOT NULL,
    id_asesor integer NOT NULL,
    id_aporte integer,
    tipo_asesoramiento character varying(50),
    canal_atencion character varying(50),
    fecha_asesoramiento timestamp without time zone,
    seguimiento_requerido boolean,
    fecha_seguimiento date,
    satisfaccion_cliente integer,
    comentarios text,
    estado character varying(20) NOT NULL
);


--
-- TOC entry 228 (class 1259 OID 17788)
-- Name: asesoramiento_financiero_id_asesoramiento_seq; Type: SEQUENCE; Schema: public; Owner: -
--

CREATE SEQUENCE public.asesoramiento_financiero_id_asesoramiento_seq
    AS integer
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


--
-- TOC entry 4687 (class 0 OID 0)
-- Dependencies: 228
-- Name: asesoramiento_financiero_id_asesoramiento_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: -
--

ALTER SEQUENCE public.asesoramiento_financiero_id_asesoramiento_seq OWNED BY public.asesoramiento_financiero.id_asesoramiento;


--
-- TOC entry 229 (class 1259 OID 17789)
-- Name: autenticacion; Type: TABLE; Schema: public; Owner: -
--

CREATE TABLE public.autenticacion (
    id_autenticacion integer NOT NULL,
    id_usuario integer NOT NULL,
    clave_sql character varying(500),
    estado_sesion character varying(20) NOT NULL,
    fecha_inicio timestamp without time zone,
    fecha_expiracion timestamp without time zone,
    ip_acceso character varying(50)
);


--
-- TOC entry 230 (class 1259 OID 17797)
-- Name: autenticacion_id_autenticacion_seq; Type: SEQUENCE; Schema: public; Owner: -
--

CREATE SEQUENCE public.autenticacion_id_autenticacion_seq
    AS integer
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


--
-- TOC entry 4688 (class 0 OID 0)
-- Dependencies: 230
-- Name: autenticacion_id_autenticacion_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: -
--

ALTER SEQUENCE public.autenticacion_id_autenticacion_seq OWNED BY public.autenticacion.id_autenticacion;


--
-- TOC entry 258 (class 1259 OID 24615)
-- Name: beneficiario_seguro; Type: TABLE; Schema: public; Owner: -
--

CREATE TABLE public.beneficiario_seguro (
    id_beneficiario integer NOT NULL,
    id_seguro integer NOT NULL,
    nombre_completo character varying(200) NOT NULL,
    parentesco character varying(50) NOT NULL,
    porcentaje numeric(5,2) NOT NULL,
    dni character varying(20),
    fecha_nacimiento date,
    telefono character varying(20),
    estado character varying(20) DEFAULT 'Activo'::character varying NOT NULL,
    CONSTRAINT beneficiario_seguro_porcentaje_check CHECK (((porcentaje > (0)::numeric) AND (porcentaje <= (100)::numeric)))
);


--
-- TOC entry 257 (class 1259 OID 24614)
-- Name: beneficiario_seguro_id_beneficiario_seq; Type: SEQUENCE; Schema: public; Owner: -
--

CREATE SEQUENCE public.beneficiario_seguro_id_beneficiario_seq
    AS integer
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


--
-- TOC entry 4689 (class 0 OID 0)
-- Dependencies: 257
-- Name: beneficiario_seguro_id_beneficiario_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: -
--

ALTER SEQUENCE public.beneficiario_seguro_id_beneficiario_seq OWNED BY public.beneficiario_seguro.id_beneficiario;


--
-- TOC entry 231 (class 1259 OID 17798)
-- Name: compania_seguro; Type: TABLE; Schema: public; Owner: -
--

CREATE TABLE public.compania_seguro (
    id_compania integer NOT NULL,
    id_institucion integer,
    nombre character varying(150) NOT NULL,
    codigo_sbs character varying(20),
    tipos_seguro_ofrecidos jsonb,
    calificacion_riesgo character varying(50),
    telefono character varying(20),
    email character varying(100),
    sitio_web character varying(200),
    estado character varying(20) NOT NULL
);


--
-- TOC entry 232 (class 1259 OID 17807)
-- Name: compania_seguro_id_compania_seq; Type: SEQUENCE; Schema: public; Owner: -
--

CREATE SEQUENCE public.compania_seguro_id_compania_seq
    AS integer
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


--
-- TOC entry 4690 (class 0 OID 0)
-- Dependencies: 232
-- Name: compania_seguro_id_compania_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: -
--

ALTER SEQUENCE public.compania_seguro_id_compania_seq OWNED BY public.compania_seguro.id_compania;


--
-- TOC entry 233 (class 1259 OID 17816)
-- Name: historial_consultas; Type: TABLE; Schema: public; Owner: -
--

CREATE TABLE public.historial_consultas (
    id_historial integer NOT NULL,
    id_usuario integer NOT NULL,
    fecha timestamp without time zone DEFAULT now() NOT NULL,
    detalle_consulta text,
    tipo_consulta character varying(50),
    resultado character varying(20),
    detalles_adicionales text
);


--
-- TOC entry 234 (class 1259 OID 17824)
-- Name: historial_consultas_id_historial_seq; Type: SEQUENCE; Schema: public; Owner: -
--

CREATE SEQUENCE public.historial_consultas_id_historial_seq
    AS integer
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


--
-- TOC entry 4691 (class 0 OID 0)
-- Dependencies: 234
-- Name: historial_consultas_id_historial_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: -
--

ALTER SEQUENCE public.historial_consultas_id_historial_seq OWNED BY public.historial_consultas.id_historial;


--
-- TOC entry 235 (class 1259 OID 17825)
-- Name: historial_rentabilidad; Type: TABLE; Schema: public; Owner: -
--

CREATE TABLE public.historial_rentabilidad (
    id_rentabilidad integer NOT NULL,
    id_afp integer NOT NULL,
    id_tipo_fondo integer NOT NULL,
    periodo character varying(10),
    rentabilidad_nominal numeric(10,2),
    rentabilidad_real numeric(10,2),
    patrimonio numeric(15,2),
    numero_afiliados integer,
    fecha_registro timestamp without time zone
);


--
-- TOC entry 236 (class 1259 OID 17831)
-- Name: historial_rentabilidad_id_rentabilidad_seq; Type: SEQUENCE; Schema: public; Owner: -
--

CREATE SEQUENCE public.historial_rentabilidad_id_rentabilidad_seq
    AS integer
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


--
-- TOC entry 4692 (class 0 OID 0)
-- Dependencies: 236
-- Name: historial_rentabilidad_id_rentabilidad_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: -
--

ALTER SEQUENCE public.historial_rentabilidad_id_rentabilidad_seq OWNED BY public.historial_rentabilidad.id_rentabilidad;


--
-- TOC entry 237 (class 1259 OID 17832)
-- Name: institucion; Type: TABLE; Schema: public; Owner: -
--

CREATE TABLE public.institucion (
    id_institucion integer NOT NULL,
    nombre character varying(150) NOT NULL,
    tipo character varying(50),
    ruc character varying(20),
    direccion character varying(200),
    telefono character varying(20),
    email character varying(100),
    sitio_web character varying(200),
    estado character varying(20) NOT NULL
);


--
-- TOC entry 238 (class 1259 OID 17840)
-- Name: institucion_id_institucion_seq; Type: SEQUENCE; Schema: public; Owner: -
--

CREATE SEQUENCE public.institucion_id_institucion_seq
    AS integer
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


--
-- TOC entry 4693 (class 0 OID 0)
-- Dependencies: 238
-- Name: institucion_id_institucion_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: -
--

ALTER SEQUENCE public.institucion_id_institucion_seq OWNED BY public.institucion.id_institucion;


--
-- TOC entry 239 (class 1259 OID 17841)
-- Name: notificacion; Type: TABLE; Schema: public; Owner: -
--

CREATE TABLE public.notificacion (
    id_notificacion integer NOT NULL,
    id_usuario integer NOT NULL,
    tipo_notificacion character varying(50),
    mensaje text,
    fecha_envio timestamp without time zone,
    fecha_lectura timestamp without time zone,
    canal character varying(30),
    prioridad character varying(20),
    estado character varying(20)
);


--
-- TOC entry 240 (class 1259 OID 17848)
-- Name: notificacion_id_notificacion_seq; Type: SEQUENCE; Schema: public; Owner: -
--

CREATE SEQUENCE public.notificacion_id_notificacion_seq
    AS integer
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


--
-- TOC entry 4694 (class 0 OID 0)
-- Dependencies: 240
-- Name: notificacion_id_notificacion_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: -
--

ALTER SEQUENCE public.notificacion_id_notificacion_seq OWNED BY public.notificacion.id_notificacion;


--
-- TOC entry 241 (class 1259 OID 17849)
-- Name: pago_seguro; Type: TABLE; Schema: public; Owner: -
--

CREATE TABLE public.pago_seguro (
    id_pago integer NOT NULL,
    id_seguro integer NOT NULL,
    numero_cuota integer,
    monto_pagado numeric(10,2),
    fecha_pago timestamp without time zone,
    metodo_pago character varying(50),
    estado character varying(20) NOT NULL,
    fecha_vencimiento timestamp without time zone,
    monto_cuota numeric(10,2),
    observaciones text
);


--
-- TOC entry 242 (class 1259 OID 17855)
-- Name: pago_seguro_id_pago_seq; Type: SEQUENCE; Schema: public; Owner: -
--

CREATE SEQUENCE public.pago_seguro_id_pago_seq
    AS integer
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


--
-- TOC entry 4695 (class 0 OID 0)
-- Dependencies: 242
-- Name: pago_seguro_id_pago_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: -
--

ALTER SEQUENCE public.pago_seguro_id_pago_seq OWNED BY public.pago_seguro.id_pago;


--
-- TOC entry 243 (class 1259 OID 17856)
-- Name: rol_usuario; Type: TABLE; Schema: public; Owner: -
--

CREATE TABLE public.rol_usuario (
    id_rol integer NOT NULL,
    nombre_rol character varying(50) NOT NULL,
    descripcion text,
    permisos jsonb,
    estado character varying(20) NOT NULL
);


--
-- TOC entry 244 (class 1259 OID 17864)
-- Name: rol_usuario_id_rol_seq; Type: SEQUENCE; Schema: public; Owner: -
--

CREATE SEQUENCE public.rol_usuario_id_rol_seq
    AS integer
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


--
-- TOC entry 4696 (class 0 OID 0)
-- Dependencies: 244
-- Name: rol_usuario_id_rol_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: -
--

ALTER SEQUENCE public.rol_usuario_id_rol_seq OWNED BY public.rol_usuario.id_rol;


--
-- TOC entry 245 (class 1259 OID 17865)
-- Name: saldo_pension; Type: TABLE; Schema: public; Owner: -
--

CREATE TABLE public.saldo_pension (
    id_saldo integer NOT NULL,
    id_usuario integer NOT NULL,
    id_afp integer,
    id_tipo_fondo integer NOT NULL,
    saldo_total numeric(15,2),
    saldo_disponible numeric(15,2),
    saldo_cic numeric(15,2),
    saldo_cv numeric(15,2),
    rentabilidad_acumulada numeric(10,2),
    fecha_corte date,
    fecha_actualizacion timestamp without time zone,
    estado character varying(20) NOT NULL
);


--
-- TOC entry 246 (class 1259 OID 17872)
-- Name: saldo_pension_id_saldo_seq; Type: SEQUENCE; Schema: public; Owner: -
--

CREATE SEQUENCE public.saldo_pension_id_saldo_seq
    AS integer
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


--
-- TOC entry 4697 (class 0 OID 0)
-- Dependencies: 246
-- Name: saldo_pension_id_saldo_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: -
--

ALTER SEQUENCE public.saldo_pension_id_saldo_seq OWNED BY public.saldo_pension.id_saldo;


--
-- TOC entry 247 (class 1259 OID 17873)
-- Name: seguro; Type: TABLE; Schema: public; Owner: -
--

CREATE TABLE public.seguro (
    id_seguro integer NOT NULL,
    id_usuario integer NOT NULL,
    id_tipo_seguro integer NOT NULL,
    id_compania integer NOT NULL,
    numero_poliza character varying(50) NOT NULL,
    numero_contrato character varying(50),
    fecha_inicio date,
    fecha_vencimiento date,
    monto_asegurado numeric(15,2),
    prima_mensual numeric(10,2),
    prima_anual numeric(10,2),
    deducible numeric(10,2),
    beneficiarios jsonb,
    coberturas jsonb,
    exclusiones jsonb,
    forma_pago character varying(30),
    metodo_pago character varying(50),
    fecha_registro timestamp without time zone,
    estado character varying(20) NOT NULL,
    dias_gracia integer DEFAULT 15,
    alerta_vencimiento boolean DEFAULT true
);


--
-- TOC entry 248 (class 1259 OID 17884)
-- Name: seguro_id_seguro_seq; Type: SEQUENCE; Schema: public; Owner: -
--

CREATE SEQUENCE public.seguro_id_seguro_seq
    AS integer
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


--
-- TOC entry 4698 (class 0 OID 0)
-- Dependencies: 248
-- Name: seguro_id_seguro_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: -
--

ALTER SEQUENCE public.seguro_id_seguro_seq OWNED BY public.seguro.id_seguro;


--
-- TOC entry 249 (class 1259 OID 17885)
-- Name: tipo_fondo; Type: TABLE; Schema: public; Owner: -
--

CREATE TABLE public.tipo_fondo (
    id_tipo_fondo integer NOT NULL,
    nombre_tipo character varying(100) NOT NULL,
    descripcion text,
    categoria character varying(50),
    rentabilidad_esperada numeric(5,2),
    estado character varying(20) NOT NULL
);


--
-- TOC entry 250 (class 1259 OID 17893)
-- Name: tipo_fondo_id_tipo_fondo_seq; Type: SEQUENCE; Schema: public; Owner: -
--

CREATE SEQUENCE public.tipo_fondo_id_tipo_fondo_seq
    AS integer
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


--
-- TOC entry 4699 (class 0 OID 0)
-- Dependencies: 250
-- Name: tipo_fondo_id_tipo_fondo_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: -
--

ALTER SEQUENCE public.tipo_fondo_id_tipo_fondo_seq OWNED BY public.tipo_fondo.id_tipo_fondo;


--
-- TOC entry 251 (class 1259 OID 17894)
-- Name: tipo_seguro; Type: TABLE; Schema: public; Owner: -
--

CREATE TABLE public.tipo_seguro (
    id_tipo_seguro integer NOT NULL,
    nombre character varying(100) NOT NULL,
    descripcion text,
    categoria character varying(50),
    cobertura_principal text,
    estado character varying(20) NOT NULL
);


--
-- TOC entry 252 (class 1259 OID 17902)
-- Name: tipo_seguro_id_tipo_seguro_seq; Type: SEQUENCE; Schema: public; Owner: -
--

CREATE SEQUENCE public.tipo_seguro_id_tipo_seguro_seq
    AS integer
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


--
-- TOC entry 4700 (class 0 OID 0)
-- Dependencies: 252
-- Name: tipo_seguro_id_tipo_seguro_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: -
--

ALTER SEQUENCE public.tipo_seguro_id_tipo_seguro_seq OWNED BY public.tipo_seguro.id_tipo_seguro;


--
-- TOC entry 256 (class 1259 OID 24587)
-- Name: tramite_seguro; Type: TABLE; Schema: public; Owner: -
--

CREATE TABLE public.tramite_seguro (
    id_tramite integer NOT NULL,
    id_usuario integer NOT NULL,
    id_seguro integer,
    tipo_tramite character varying(50) NOT NULL,
    descripcion text NOT NULL,
    fecha_solicitud timestamp without time zone DEFAULT CURRENT_TIMESTAMP NOT NULL,
    fecha_resolucion timestamp without time zone,
    estado character varying(20) DEFAULT 'Pendiente'::character varying NOT NULL,
    respuesta text,
    documentos_adjuntos jsonb,
    prioridad character varying(20) DEFAULT 'Media'::character varying,
    canal_atencion character varying(50)
);


--
-- TOC entry 255 (class 1259 OID 24586)
-- Name: tramite_seguro_id_tramite_seq; Type: SEQUENCE; Schema: public; Owner: -
--

CREATE SEQUENCE public.tramite_seguro_id_tramite_seq
    AS integer
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


--
-- TOC entry 4701 (class 0 OID 0)
-- Dependencies: 255
-- Name: tramite_seguro_id_tramite_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: -
--

ALTER SEQUENCE public.tramite_seguro_id_tramite_seq OWNED BY public.tramite_seguro.id_tramite;


--
-- TOC entry 253 (class 1259 OID 17903)
-- Name: usuario; Type: TABLE; Schema: public; Owner: -
--

CREATE TABLE public.usuario (
    id_usuario integer NOT NULL,
    id_rol integer NOT NULL,
    nombre character varying(100) NOT NULL,
    apellido character varying(100) NOT NULL,
    dni character varying(20) NOT NULL,
    fecha_nacimiento date,
    genero character varying(5),
    estado_civil character varying(20),
    correo character varying(100) NOT NULL,
    telefono character varying(20),
    direccion character varying(200),
    distrito character varying(100),
    provincia character varying(100),
    departamento character varying(100),
    contrasena character varying(255),
    clave_sol character varying(100),
    centro_trabajo character varying(150),
    salario_actual numeric(10,2),
    fecha_ingreso_trabajo date,
    tipo_contrato character varying(30),
    cuspp character varying(50),
    id_afp integer,
    tipo_regimen character varying(20),
    fecha_afiliacion date,
    notificaciones_email boolean,
    notificaciones_sms boolean,
    estado character varying(20) NOT NULL,
    ultimo_acceso timestamp without time zone,
    foto_perfil text
);


--
-- TOC entry 254 (class 1259 OID 17915)
-- Name: usuario_id_usuario_seq; Type: SEQUENCE; Schema: public; Owner: -
--

CREATE SEQUENCE public.usuario_id_usuario_seq
    AS integer
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


--
-- TOC entry 4702 (class 0 OID 0)
-- Dependencies: 254
-- Name: usuario_id_usuario_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: -
--

ALTER SEQUENCE public.usuario_id_usuario_seq OWNED BY public.usuario.id_usuario;


--
-- TOC entry 4385 (class 2604 OID 17916)
-- Name: administrador id_admin; Type: DEFAULT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.administrador ALTER COLUMN id_admin SET DEFAULT nextval('public.administrador_id_admin_seq'::regclass);


--
-- TOC entry 4386 (class 2604 OID 17917)
-- Name: afp id_afp; Type: DEFAULT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.afp ALTER COLUMN id_afp SET DEFAULT nextval('public.afp_id_afp_seq'::regclass);


--
-- TOC entry 4387 (class 2604 OID 17918)
-- Name: aporte_pension id_aporte; Type: DEFAULT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.aporte_pension ALTER COLUMN id_aporte SET DEFAULT nextval('public.aporte_pension_id_aporte_seq'::regclass);


--
-- TOC entry 4388 (class 2604 OID 17919)
-- Name: asesor id_asesor; Type: DEFAULT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.asesor ALTER COLUMN id_asesor SET DEFAULT nextval('public.asesor_id_asesor_seq'::regclass);


--
-- TOC entry 4389 (class 2604 OID 17920)
-- Name: asesoramiento_financiero id_asesoramiento; Type: DEFAULT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.asesoramiento_financiero ALTER COLUMN id_asesoramiento SET DEFAULT nextval('public.asesoramiento_financiero_id_asesoramiento_seq'::regclass);


--
-- TOC entry 4390 (class 2604 OID 17921)
-- Name: autenticacion id_autenticacion; Type: DEFAULT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.autenticacion ALTER COLUMN id_autenticacion SET DEFAULT nextval('public.autenticacion_id_autenticacion_seq'::regclass);


--
-- TOC entry 4410 (class 2604 OID 24618)
-- Name: beneficiario_seguro id_beneficiario; Type: DEFAULT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.beneficiario_seguro ALTER COLUMN id_beneficiario SET DEFAULT nextval('public.beneficiario_seguro_id_beneficiario_seq'::regclass);


--
-- TOC entry 4391 (class 2604 OID 17922)
-- Name: compania_seguro id_compania; Type: DEFAULT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.compania_seguro ALTER COLUMN id_compania SET DEFAULT nextval('public.compania_seguro_id_compania_seq'::regclass);


--
-- TOC entry 4392 (class 2604 OID 17924)
-- Name: historial_consultas id_historial; Type: DEFAULT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.historial_consultas ALTER COLUMN id_historial SET DEFAULT nextval('public.historial_consultas_id_historial_seq'::regclass);


--
-- TOC entry 4394 (class 2604 OID 17925)
-- Name: historial_rentabilidad id_rentabilidad; Type: DEFAULT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.historial_rentabilidad ALTER COLUMN id_rentabilidad SET DEFAULT nextval('public.historial_rentabilidad_id_rentabilidad_seq'::regclass);


--
-- TOC entry 4395 (class 2604 OID 17926)
-- Name: institucion id_institucion; Type: DEFAULT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.institucion ALTER COLUMN id_institucion SET DEFAULT nextval('public.institucion_id_institucion_seq'::regclass);


--
-- TOC entry 4396 (class 2604 OID 17927)
-- Name: notificacion id_notificacion; Type: DEFAULT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.notificacion ALTER COLUMN id_notificacion SET DEFAULT nextval('public.notificacion_id_notificacion_seq'::regclass);


--
-- TOC entry 4397 (class 2604 OID 17928)
-- Name: pago_seguro id_pago; Type: DEFAULT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.pago_seguro ALTER COLUMN id_pago SET DEFAULT nextval('public.pago_seguro_id_pago_seq'::regclass);


--
-- TOC entry 4398 (class 2604 OID 17929)
-- Name: rol_usuario id_rol; Type: DEFAULT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.rol_usuario ALTER COLUMN id_rol SET DEFAULT nextval('public.rol_usuario_id_rol_seq'::regclass);


--
-- TOC entry 4399 (class 2604 OID 17930)
-- Name: saldo_pension id_saldo; Type: DEFAULT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.saldo_pension ALTER COLUMN id_saldo SET DEFAULT nextval('public.saldo_pension_id_saldo_seq'::regclass);


--
-- TOC entry 4400 (class 2604 OID 17931)
-- Name: seguro id_seguro; Type: DEFAULT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.seguro ALTER COLUMN id_seguro SET DEFAULT nextval('public.seguro_id_seguro_seq'::regclass);


--
-- TOC entry 4403 (class 2604 OID 17932)
-- Name: tipo_fondo id_tipo_fondo; Type: DEFAULT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.tipo_fondo ALTER COLUMN id_tipo_fondo SET DEFAULT nextval('public.tipo_fondo_id_tipo_fondo_seq'::regclass);


--
-- TOC entry 4404 (class 2604 OID 17933)
-- Name: tipo_seguro id_tipo_seguro; Type: DEFAULT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.tipo_seguro ALTER COLUMN id_tipo_seguro SET DEFAULT nextval('public.tipo_seguro_id_tipo_seguro_seq'::regclass);


--
-- TOC entry 4406 (class 2604 OID 24590)
-- Name: tramite_seguro id_tramite; Type: DEFAULT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.tramite_seguro ALTER COLUMN id_tramite SET DEFAULT nextval('public.tramite_seguro_id_tramite_seq'::regclass);


--
-- TOC entry 4405 (class 2604 OID 17934)
-- Name: usuario id_usuario; Type: DEFAULT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.usuario ALTER COLUMN id_usuario SET DEFAULT nextval('public.usuario_id_usuario_seq'::regclass);


--
-- TOC entry 4637 (class 0 OID 17741)
-- Dependencies: 219
-- Data for Name: administrador; Type: TABLE DATA; Schema: public; Owner: -
--

COPY public.administrador (id_admin, id_usuario, rol_detalle, permisos_sistema, estado) FROM stdin;
1	1	Super Admin	{"respaldos": true, "gestion_usuarios": true, "reportes_globales": true, "configuracion_sistema": true}	Activo
\.


--
-- TOC entry 4639 (class 0 OID 17750)
-- Dependencies: 221
-- Data for Name: afp; Type: TABLE DATA; Schema: public; Owner: -
--

COPY public.afp (id_afp, id_institucion, nombre, codigo_sbs, comision_flujo, comision_saldo, comision_mixta, rentabilidad_promedio, fondos_disponibles, estado) FROM stdin;
4	5	AFP Profuturo	SBS004	1.47	1.20	\N	6.50	["1", "2", "3"]	Activo
2	3	AFP Prima	SBS002	0.99	1.20	\N	7.80	["1", "2", "3"]	Activo
1	2	AFP Integra	SBS001	0.85	1.20	\N	7.50	["1", "2", "3"]	Activo
3	4	AFP Habitat	SBS003	1.25	1.20	\N	6.50	["1", "2", "3"]	Activo
\.


--
-- TOC entry 4641 (class 0 OID 17760)
-- Dependencies: 223
-- Data for Name: aporte_pension; Type: TABLE DATA; Schema: public; Owner: -
--

COPY public.aporte_pension (id_aporte, id_usuario, id_institucion, id_tipo_fondo, cuspp, periodo, monto_aporte, aporte_trabajador, aporte_empleador, comision_cobrada, seguro_invalidez, fecha_aporte, empleador, ruc_empleador, salario_declarado, dias_trabajados, observaciones, estado) FROM stdin;
2	1	1	1	\N	2024-12	850.00	425.00	425.00	0.00	0.00	2024-12-31 00:00:00	Empresa ACME S.A.C.	20123456001	2500.00	30	Aporte ONP diciembre	Procesado
3	1	1	1	\N	2024-11	850.00	425.00	425.00	0.00	0.00	2024-11-30 00:00:00	Empresa ACME S.A.C.	20123456001	2500.00	30	Aporte ONP noviembre	Procesado
4	1	1	1	\N	2024-10	850.00	425.00	425.00	0.00	0.00	2024-10-31 00:00:00	Empresa ACME S.A.C.	20123456001	2500.00	30	Aporte ONP octubre	Procesado
5	1	1	1	\N	2024-09	850.00	425.00	425.00	0.00	0.00	2024-09-30 00:00:00	Empresa ACME S.A.C.	20123456001	2500.00	30	Aporte ONP septiembre	Procesado
6	1	1	1	\N	2024-08	850.00	425.00	425.00	0.00	0.00	2024-08-31 00:00:00	Empresa ACME S.A.C.	20123456001	2500.00	30	Aporte ONP agosto	Procesado
7	1	1	1	\N	2024-07	850.00	425.00	425.00	0.00	0.00	2024-07-31 00:00:00	Empresa ACME S.A.C.	20123456001	2500.00	30	Aporte ONP julio	Procesado
8	1	1	1	\N	2024-06	850.00	425.00	425.00	0.00	0.00	2024-06-30 00:00:00	Empresa ACME S.A.C.	20123456001	2500.00	30	Aporte ONP junio	Procesado
10	1	1	1	\N	2024-04	850.00	425.00	425.00	0.00	0.00	2024-04-30 00:00:00	Empresa ACME S.A.C.	20123456001	2500.00	30	Aporte ONP abril	Procesado
11	1	1	1	\N	2024-03	850.00	425.00	425.00	0.00	0.00	2024-03-31 00:00:00	Empresa ACME S.A.C.	20123456001	2500.00	30	Aporte ONP marzo	Procesado
17	6	2	2	\N	2025-01	1111.00	1111.00	0.00	50.00	33.00	2025-01-16 00:00:00	RED GAS SAC	988654637	3000.00	30	Prueba onp	Registrado
15	6	2	2	\N	2025-09	500.00	500.00	0.00	44.00	50.00	2025-10-10 00:00:00	RED GAS 	876785919	4000.00	30	Prueba	Registrado
18	6	1	2	\N	2025-09	1000.00	1000.00	0.00	45.00	78.00	2025-10-26 00:00:00	UTP SAC	455566778	30000.00	30	Ninguna	Registrado
19	6	2	2	\N	2025-01	3000.00	3000.00	0.00	50.00	56.00	2025-10-21 00:00:00	OLVA CURRIER	66553346	6000.00	30	Ninguna	Registrado
16	6	2	2	\N	2025-01	1111.00	1111.00	0.00	50.00	33.00	2025-01-16 00:00:00	RED GAS SAC	988654637	3000.00	30	Prueba onp	Procesado
1	1	2	2	\N	2025-01	858.00	425.00	425.00	0.00	0.00	2025-01-31 00:00:00	Empresa ACME S.A.C.	20123456001	2500.00	30	Aporte ONP enero	Procesado
9	1	1	1	\N	2024-05	850.00	425.00	425.00	0.00	0.00	2024-05-31 00:00:00	Empresa ACME S.A.C.	20123456001	2500.00	30	Aporte ONP mayo	Observado
21	12	2	2	\N	2025-02	1000.00	500.00	500.00	55.00	50.00	2025-10-14 00:00:00	RED GAS SAC	20123456222	5000.00	30		Procesado
20	1	2	2	\N	2025-02	1000.00	500.00	500.00	44.00	55.00	2025-10-15 00:00:00	RED GAS SAC	20123456222	5000.00	29	Ninguna	Registrado
12	1	2	2	\N	2024-02	844.99	419.99	425.00	0.00	0.00	2024-02-29 00:00:00	Empresa ACME S.A.C.	20123456001	2500.00	29	Aporte ONP febrero	Registrado
22	6	2	2	\N	2025-12	1000.00	500.00	500.00	0.00	0.00	2025-12-01 00:00:00	SHALOM SAC	69854522547	3000.00	30		Registrado
\.


--
-- TOC entry 4643 (class 0 OID 17771)
-- Dependencies: 225
-- Data for Name: asesor; Type: TABLE DATA; Schema: public; Owner: -
--

COPY public.asesor (id_asesor, id_usuario, especialidad, codigo_asesor, estado) FROM stdin;
\.


--
-- TOC entry 4645 (class 0 OID 17779)
-- Dependencies: 227
-- Data for Name: asesoramiento_financiero; Type: TABLE DATA; Schema: public; Owner: -
--

COPY public.asesoramiento_financiero (id_asesoramiento, id_usuario, id_asesor, id_aporte, tipo_asesoramiento, canal_atencion, fecha_asesoramiento, seguimiento_requerido, fecha_seguimiento, satisfaccion_cliente, comentarios, estado) FROM stdin;
\.


--
-- TOC entry 4647 (class 0 OID 17789)
-- Dependencies: 229
-- Data for Name: autenticacion; Type: TABLE DATA; Schema: public; Owner: -
--

COPY public.autenticacion (id_autenticacion, id_usuario, clave_sql, estado_sesion, fecha_inicio, fecha_expiracion, ip_acceso) FROM stdin;
\.


--
-- TOC entry 4676 (class 0 OID 24615)
-- Dependencies: 258
-- Data for Name: beneficiario_seguro; Type: TABLE DATA; Schema: public; Owner: -
--

COPY public.beneficiario_seguro (id_beneficiario, id_seguro, nombre_completo, parentesco, porcentaje, dni, fecha_nacimiento, telefono, estado) FROM stdin;
1	8	María García López	Cónyuge	100.00	87654321	1988-05-20	965432187	Activo
2	9	María García López	Cónyuge	60.00	87654321	1988-05-20	965432187	Activo
3	9	Juan Carlos Mendoza García	Hijo	40.00	\N	2015-03-10	\N	Activo
4	14	María García López	Cónyuge	100.00	87654321	1988-05-20	965432187	Activo
5	23	Orlando	Hijo	50.00	12344567	2004-06-15	99999999	Activo
\.


--
-- TOC entry 4649 (class 0 OID 17798)
-- Dependencies: 231
-- Data for Name: compania_seguro; Type: TABLE DATA; Schema: public; Owner: -
--

COPY public.compania_seguro (id_compania, id_institucion, nombre, codigo_sbs, tipos_seguro_ofrecidos, calificacion_riesgo, telefono, email, sitio_web, estado) FROM stdin;
1	1	Rimac Seguros	CS001	["Vehicular", "Hogar", "Vida"]	AAA	01-411-1111	contacto@rimac.com	https://www.rimac.com	Activo
2	1	Pacífico Seguros	CS002	["Vehicular", "Hogar", "Salud"]	AA+	01-513-5000	atencion@pacifico.com.pe	https://www.pacifico.com.pe	Activo
3	1	Mapfre Perú	CS003	["Vehicular", "Salud", "Vida"]	AA	01-617-0202	contacto@mapfre.com.pe	https://www.mapfre.com.pe	Activo
8	\N	Pruebacompañia	CS004	\N	AA-	22222222	prueba122345@gmail.com	https://www.youtube.com/	Activo
\.


--
-- TOC entry 4651 (class 0 OID 17816)
-- Dependencies: 233
-- Data for Name: historial_consultas; Type: TABLE DATA; Schema: public; Owner: -
--

COPY public.historial_consultas (id_historial, id_usuario, fecha, detalle_consulta, tipo_consulta, resultado, detalles_adicionales) FROM stdin;
1	1	2025-10-27 13:28:37.941813	Comparativo ONP vs AFP - ONP: S/ 9350.00, AFP: S/ 1858.00	Rentabilidad	Exitoso	{"onp":9350.00,"afp":1858.00,"total":11208.00,"porcentajeONP":83.42,"porcentajeAFP":16.58}
2	1	2025-10-27 13:28:40.352645	Consulta resumen financiero - Saldo: S/ 11208.00, Proyección: S/ 46.70	Proyección	Exitoso	{"saldoTotal":11208.00,"saldoDisponible":10087.20,"proyeccion":46.70,"aportesONP":9350.00,"aportesAFP":1858.00}
3	1	2025-10-27 13:28:47.760399	Comparativo ONP vs AFP - ONP: S/ 9350.00, AFP: S/ 1858.00	Rentabilidad	Exitoso	{"onp":9350.00,"afp":1858.00,"total":11208.00,"porcentajeONP":83.42,"porcentajeAFP":16.58}
4	1	2025-10-27 13:28:48.05131	Consulta resumen financiero - Saldo: S/ 11208.00, Proyección: S/ 46.70	Proyección	Exitoso	{"saldoTotal":11208.00,"saldoDisponible":10087.20,"proyeccion":46.70,"aportesONP":9350.00,"aportesAFP":1858.00}
5	1	2025-10-27 13:28:52.592257	Comparativo ONP vs AFP - ONP: S/ 9350.00, AFP: S/ 1858.00	Rentabilidad	Exitoso	{"onp":9350.00,"afp":1858.00,"total":11208.00,"porcentajeONP":83.42,"porcentajeAFP":16.58}
6	1	2025-10-27 13:40:05.961341	Consulta resumen financiero - Saldo: S/ 11208.00, Proyección: S/ 46.70	Proyección	Exitoso	{"saldoTotal":11208.00,"saldoDisponible":10087.20,"proyeccion":46.70,"aportesONP":9350.00,"aportesAFP":1858.00}
7	1	2025-10-27 13:40:06.395524	Comparativo ONP vs AFP - ONP: S/ 9350.00, AFP: S/ 1858.00	Rentabilidad	Exitoso	{"onp":9350.00,"afp":1858.00,"total":11208.00,"porcentajeONP":83.42,"porcentajeAFP":16.58}
8	1	2025-10-27 16:02:17.42772	Consulta resumen financiero - Saldo: S/ 11208.00, Proyección: S/ 46.70	Proyección	Exitoso	{"saldoTotal":11208.00,"saldoDisponible":10087.20,"proyeccion":46.70,"aportesONP":9350.00,"aportesAFP":1858.00}
9	1	2025-10-27 16:02:19.767595	Comparativo ONP vs AFP - ONP: S/ 9350.00, AFP: S/ 1858.00	Rentabilidad	Exitoso	{"onp":9350.00,"afp":1858.00,"total":11208.00,"porcentajeONP":83.42,"porcentajeAFP":16.58}
10	1	2025-10-27 23:47:31.191843	Consulta resumen financiero - Saldo: S/ 11208.00, Proyección: S/ 46.70	Proyección	Exitoso	{"saldoTotal":11208.00,"saldoDisponible":10087.20,"proyeccion":46.70,"aportesONP":9350.00,"aportesAFP":1858.00}
11	1	2025-10-27 23:47:31.965907	Comparativo ONP vs AFP - ONP: S/ 9350.00, AFP: S/ 1858.00	Rentabilidad	Exitoso	{"onp":9350.00,"afp":1858.00,"total":11208.00,"porcentajeONP":83.42,"porcentajeAFP":16.58}
12	1	2025-10-27 23:47:34.367081	Consulta resumen financiero - Saldo: S/ 11208.00, Proyección: S/ 46.70	Proyección	Exitoso	{"saldoTotal":11208.00,"saldoDisponible":10087.20,"proyeccion":46.70,"aportesONP":9350.00,"aportesAFP":1858.00}
13	1	2025-10-27 23:47:35.070717	Comparativo ONP vs AFP - ONP: S/ 9350.00, AFP: S/ 1858.00	Rentabilidad	Exitoso	{"onp":9350.00,"afp":1858.00,"total":11208.00,"porcentajeONP":83.42,"porcentajeAFP":16.58}
14	1	2025-10-28 00:37:24.404589	Comparativo ONP vs AFP - ONP: S/ 9350.00, AFP: S/ 1858.00	Rentabilidad	Exitoso	{"onp":9350.00,"afp":1858.00,"total":11208.00,"porcentajeONP":83.42,"porcentajeAFP":16.58}
15	1	2025-10-28 00:37:25.511606	Consulta resumen financiero - Saldo: S/ 11208.00, Proyección: S/ 46.70	Proyección	Exitoso	{"saldoTotal":11208.00,"saldoDisponible":10087.20,"proyeccion":46.70,"aportesONP":9350.00,"aportesAFP":1858.00}
16	1	2025-10-28 04:09:30.461931	Consulta resumen financiero - Saldo: S/ 11208.00, Proyección: S/ 46.70	Proyección	Exitoso	{"saldoTotal":11208.00,"saldoDisponible":10087.20,"proyeccion":46.70,"aportesONP":9350.00,"aportesAFP":1858.00}
17	1	2025-10-28 04:09:30.710021	Comparativo ONP vs AFP - ONP: S/ 9350.00, AFP: S/ 1858.00	Rentabilidad	Exitoso	{"onp":9350.00,"afp":1858.00,"total":11208.00,"porcentajeONP":83.42,"porcentajeAFP":16.58}
18	1	2025-10-28 04:12:01.796957	Comparativo ONP vs AFP - ONP: S/ 9350.00, AFP: S/ 1858.00	Rentabilidad	Exitoso	{"onp":9350.00,"afp":1858.00,"total":11208.00,"porcentajeONP":83.42,"porcentajeAFP":16.58}
19	1	2025-10-28 04:12:02.730457	Consulta resumen financiero - Saldo: S/ 11208.00, Proyección: S/ 46.70	Proyección	Exitoso	{"saldoTotal":11208.00,"saldoDisponible":10087.20,"proyeccion":46.70,"aportesONP":9350.00,"aportesAFP":1858.00}
20	1	2025-10-28 04:19:00.376471	Consulta resumen financiero - Saldo: S/ 11208.00, Proyección: S/ 46.70	Proyección	Exitoso	{"saldoTotal":11208.00,"saldoDisponible":10087.20,"proyeccion":46.70,"aportesONP":9350.00,"aportesAFP":1858.00}
21	1	2025-10-28 04:19:01.02233	Comparativo ONP vs AFP - ONP: S/ 9350.00, AFP: S/ 1858.00	Rentabilidad	Exitoso	{"onp":9350.00,"afp":1858.00,"total":11208.00,"porcentajeONP":83.42,"porcentajeAFP":16.58}
22	1	2025-10-28 04:19:21.130156	Consulta resumen financiero - Saldo: S/ 11208.00, Proyección: S/ 46.70	Proyección	Exitoso	{"saldoTotal":11208.00,"saldoDisponible":10087.20,"proyeccion":46.70,"aportesONP":9350.00,"aportesAFP":1858.00}
23	1	2025-10-28 04:19:22.163019	Comparativo ONP vs AFP - ONP: S/ 9350.00, AFP: S/ 1858.00	Rentabilidad	Exitoso	{"onp":9350.00,"afp":1858.00,"total":11208.00,"porcentajeONP":83.42,"porcentajeAFP":16.58}
24	1	2025-10-28 04:19:32.158551	Comparativo ONP vs AFP - ONP: S/ 9350.00, AFP: S/ 1858.00	Rentabilidad	Exitoso	{"onp":9350.00,"afp":1858.00,"total":11208.00,"porcentajeONP":83.42,"porcentajeAFP":16.58}
25	1	2025-10-28 04:24:46.150599	Consulta resumen financiero - Saldo: S/ 11208.00, Proyección: S/ 46.70	Proyección	Exitoso	{"saldoTotal":11208.00,"saldoDisponible":10087.20,"proyeccion":46.70,"aportesONP":9350.00,"aportesAFP":1858.00}
26	1	2025-10-28 04:24:46.832198	Comparativo ONP vs AFP - ONP: S/ 9350.00, AFP: S/ 1858.00	Rentabilidad	Exitoso	{"onp":9350.00,"afp":1858.00,"total":11208.00,"porcentajeONP":83.42,"porcentajeAFP":16.58}
27	1	2025-10-28 04:26:32.610968	Consulta resumen financiero - Saldo: S/ 11202.99, Proyección: S/ 46.68	Proyección	Exitoso	{"saldoTotal":11202.99,"saldoDisponible":10082.69,"proyeccion":46.68,"aportesONP":8500.00,"aportesAFP":2702.99}
28	1	2025-10-28 04:26:32.851245	Comparativo ONP vs AFP - ONP: S/ 8500.00, AFP: S/ 2702.99	Rentabilidad	Exitoso	{"onp":8500.00,"afp":2702.99,"total":11202.99,"porcentajeONP":75.87,"porcentajeAFP":24.13}
29	1	2025-10-28 07:54:42.955233	Consulta resumen financiero - Saldo: S/ 11202.99, Proyección: S/ 46.68	Proyección	Exitoso	{"saldoTotal":11202.99,"saldoDisponible":10082.69,"proyeccion":46.68,"aportesONP":8500.00,"aportesAFP":2702.99}
30	1	2025-10-28 07:54:43.604673	Comparativo ONP vs AFP - ONP: S/ 8500.00, AFP: S/ 2702.99	Rentabilidad	Exitoso	{"onp":8500.00,"afp":2702.99,"total":11202.99,"porcentajeONP":75.87,"porcentajeAFP":24.13}
31	1	2025-10-28 07:54:49.570984	Comparativo ONP vs AFP - ONP: S/ 8500.00, AFP: S/ 2702.99	Rentabilidad	Exitoso	{"onp":8500.00,"afp":2702.99,"total":11202.99,"porcentajeONP":75.87,"porcentajeAFP":24.13}
32	6	2025-10-28 09:27:46.459731	Consulta resumen financiero - Saldo: S/ 6722.00, Proyección: S/ 28.01	Proyección	Exitoso	{"saldoTotal":6722.00,"saldoDisponible":6049.80,"proyeccion":28.01,"aportesONP":1000.00,"aportesAFP":5722.00}
33	6	2025-10-28 09:27:46.566607	Comparativo ONP vs AFP - ONP: S/ 1000.00, AFP: S/ 5722.00	Rentabilidad	Exitoso	{"onp":1000.00,"afp":5722.00,"total":6722.00,"porcentajeONP":14.88,"porcentajeAFP":85.12}
34	6	2025-10-28 09:27:55.545161	Comparativo ONP vs AFP - ONP: S/ 1000.00, AFP: S/ 5722.00	Rentabilidad	Exitoso	{"onp":1000.00,"afp":5722.00,"total":6722.00,"porcentajeONP":14.88,"porcentajeAFP":85.12}
35	1	2025-10-30 09:13:19.676283	Consulta resumen financiero - Saldo: S/ 11202.99, Proyección: S/ 46.68	Proyección	Exitoso	{"saldoTotal":11202.99,"saldoDisponible":10082.69,"proyeccion":46.68,"aportesONP":8500.00,"aportesAFP":2702.99}
36	1	2025-10-30 09:13:20.992775	Comparativo ONP vs AFP - ONP: S/ 8500.00, AFP: S/ 2702.99	Rentabilidad	Exitoso	{"onp":8500.00,"afp":2702.99,"total":11202.99,"porcentajeONP":75.87,"porcentajeAFP":24.13}
37	6	2025-10-30 09:15:59.936603	Consulta resumen financiero - Saldo: S/ 6722.00, Proyección: S/ 28.01	Proyección	Exitoso	{"saldoTotal":6722.00,"saldoDisponible":6049.80,"proyeccion":28.01,"aportesONP":1000.00,"aportesAFP":5722.00}
38	6	2025-10-30 09:16:00.066284	Comparativo ONP vs AFP - ONP: S/ 1000.00, AFP: S/ 5722.00	Rentabilidad	Exitoso	{"onp":1000.00,"afp":5722.00,"total":6722.00,"porcentajeONP":14.88,"porcentajeAFP":85.12}
39	6	2025-10-30 09:16:05.032011	Consulta resumen financiero - Saldo: S/ 6722.00, Proyección: S/ 28.01	Proyección	Exitoso	{"saldoTotal":6722.00,"saldoDisponible":6049.80,"proyeccion":28.01,"aportesONP":1000.00,"aportesAFP":5722.00}
40	6	2025-10-30 09:16:05.699149	Comparativo ONP vs AFP - ONP: S/ 1000.00, AFP: S/ 5722.00	Rentabilidad	Exitoso	{"onp":1000.00,"afp":5722.00,"total":6722.00,"porcentajeONP":14.88,"porcentajeAFP":85.12}
41	6	2025-10-30 09:19:32.149583	Consulta resumen financiero - Saldo: S/ 6722.00, Proyección: S/ 28.01	Proyección	Exitoso	{"saldoTotal":6722.00,"saldoDisponible":6049.80,"proyeccion":28.01,"aportesONP":1000.00,"aportesAFP":5722.00}
42	6	2025-10-30 09:19:32.446611	Comparativo ONP vs AFP - ONP: S/ 1000.00, AFP: S/ 5722.00	Rentabilidad	Exitoso	{"onp":1000.00,"afp":5722.00,"total":6722.00,"porcentajeONP":14.88,"porcentajeAFP":85.12}
43	6	2025-10-30 09:39:08.332261	Comparativo ONP vs AFP - ONP: S/ 1000.00, AFP: S/ 5722.00	Rentabilidad	Exitoso	{"onp":1000.00,"afp":5722.00,"total":6722.00,"porcentajeONP":14.88,"porcentajeAFP":85.12}
44	6	2025-10-30 09:39:10.791085	Consulta resumen financiero - Saldo: S/ 6722.00, Proyección: S/ 28.01	Proyección	Exitoso	{"saldoTotal":6722.00,"saldoDisponible":6049.80,"proyeccion":28.01,"aportesONP":1000.00,"aportesAFP":5722.00}
45	6	2025-10-30 09:39:19.605128	Comparativo ONP vs AFP - ONP: S/ 1000.00, AFP: S/ 5722.00	Rentabilidad	Exitoso	{"onp":1000.00,"afp":5722.00,"total":6722.00,"porcentajeONP":14.88,"porcentajeAFP":85.12}
46	6	2025-11-27 02:14:42.606623	Comparativo ONP vs AFP - ONP: S/ 1000.00, AFP: S/ 5722.00	Rentabilidad	Exitoso	{"onp":1000.00,"afp":5722.00,"total":6722.00,"porcentajeONP":14.88,"porcentajeAFP":85.12}
47	6	2025-11-27 02:14:42.952683	Consulta resumen financiero - Saldo: S/ 6722.00, Proyección: S/ 28.01	Proyección	Exitoso	{"saldoTotal":6722.00,"saldoDisponible":6049.80,"proyeccion":28.01,"aportesONP":1000.00,"aportesAFP":5722.00}
48	6	2025-11-27 02:14:46.467948	Comparativo ONP vs AFP - ONP: S/ 1000.00, AFP: S/ 5722.00	Rentabilidad	Exitoso	{"onp":1000.00,"afp":5722.00,"total":6722.00,"porcentajeONP":14.88,"porcentajeAFP":85.12}
49	6	2025-11-27 02:32:41.726556	Consulta resumen financiero - Saldo: S/ 6722.00, Proyección: S/ 28.01	Proyección	Exitoso	{"saldoTotal":6722.00,"saldoDisponible":6049.80,"proyeccion":28.01,"aportesONP":1000.00,"aportesAFP":5722.00}
50	6	2025-11-27 02:32:43.098582	Comparativo ONP vs AFP - ONP: S/ 1000.00, AFP: S/ 5722.00	Rentabilidad	Exitoso	{"onp":1000.00,"afp":5722.00,"total":6722.00,"porcentajeONP":14.88,"porcentajeAFP":85.12}
51	6	2025-11-27 02:32:51.0743	Comparativo ONP vs AFP - ONP: S/ 1000.00, AFP: S/ 5722.00	Rentabilidad	Exitoso	{"onp":1000.00,"afp":5722.00,"total":6722.00,"porcentajeONP":14.88,"porcentajeAFP":85.12}
52	6	2025-11-27 02:35:57.505937	Comparativo ONP vs AFP - ONP: S/ 1000.00, AFP: S/ 5722.00	Rentabilidad	Exitoso	{"onp":1000.00,"afp":5722.00,"total":6722.00,"porcentajeONP":14.88,"porcentajeAFP":85.12}
53	6	2025-11-27 02:36:00.822456	Comparativo ONP vs AFP - ONP: S/ 1000.00, AFP: S/ 5722.00	Rentabilidad	Exitoso	{"onp":1000.00,"afp":5722.00,"total":6722.00,"porcentajeONP":14.88,"porcentajeAFP":85.12}
54	6	2025-11-27 02:56:54.424427	Aporte registrado: 2025-12 - S/ 1000.0	Aporte	Exitoso	{"idAporte":22,"periodo":"2025-12","monto":1000.00,"trabajador":500.00,"empleador":500.00,"institucion":"null"}
55	6	2025-11-27 03:47:13.791494	Consulta resumen financiero - Saldo: S/ 7722.00, Proyección: S/ 32.18	Proyección	Exitoso	{"saldoTotal":7722.00,"saldoDisponible":6949.80,"proyeccion":32.18,"aportesONP":1000.00,"aportesAFP":6722.00}
56	6	2025-11-27 03:47:14.41782	Comparativo ONP vs AFP - ONP: S/ 1000.00, AFP: S/ 6722.00	Rentabilidad	Exitoso	{"onp":1000.00,"afp":6722.00,"total":7722.00,"porcentajeONP":12.95,"porcentajeAFP":87.05}
57	6	2025-11-27 03:47:53.684582	Comparativo ONP vs AFP - ONP: S/ 1000.00, AFP: S/ 6722.00	Rentabilidad	Exitoso	{"onp":1000.00,"afp":6722.00,"total":7722.00,"porcentajeONP":12.95,"porcentajeAFP":87.05}
58	6	2025-11-27 03:47:54.755911	Consulta resumen financiero - Saldo: S/ 7722.00, Proyección: S/ 32.18	Proyección	Exitoso	{"saldoTotal":7722.00,"saldoDisponible":6949.80,"proyeccion":32.18,"aportesONP":1000.00,"aportesAFP":6722.00}
59	6	2025-11-27 03:47:57.474892	Comparativo ONP vs AFP - ONP: S/ 1000.00, AFP: S/ 6722.00	Rentabilidad	Exitoso	{"onp":1000.00,"afp":6722.00,"total":7722.00,"porcentajeONP":12.95,"porcentajeAFP":87.05}
60	6	2025-11-27 03:58:09.428929	Comparativo ONP vs AFP - ONP: S/ 1000.00, AFP: S/ 6722.00	Rentabilidad	Exitoso	{"onp":1000.00,"afp":6722.00,"total":7722.00,"porcentajeONP":12.95,"porcentajeAFP":87.05}
61	6	2025-11-27 03:58:09.770467	Consulta resumen financiero - Saldo: S/ 7722.00, Proyección: S/ 32.18	Proyección	Exitoso	{"saldoTotal":7722.00,"saldoDisponible":6949.80,"proyeccion":32.18,"aportesONP":1000.00,"aportesAFP":6722.00}
62	6	2025-11-27 03:58:14.128867	Comparativo ONP vs AFP - ONP: S/ 1000.00, AFP: S/ 6722.00	Rentabilidad	Exitoso	{"onp":1000.00,"afp":6722.00,"total":7722.00,"porcentajeONP":12.95,"porcentajeAFP":87.05}
\.


--
-- TOC entry 4653 (class 0 OID 17825)
-- Dependencies: 235
-- Data for Name: historial_rentabilidad; Type: TABLE DATA; Schema: public; Owner: -
--

COPY public.historial_rentabilidad (id_rentabilidad, id_afp, id_tipo_fondo, periodo, rentabilidad_nominal, rentabilidad_real, patrimonio, numero_afiliados, fecha_registro) FROM stdin;
1	3	1	2025-10	6.50	6.50	\N	\N	2025-10-27 14:01:59.847335
\.


--
-- TOC entry 4655 (class 0 OID 17832)
-- Dependencies: 237
-- Data for Name: institucion; Type: TABLE DATA; Schema: public; Owner: -
--

COPY public.institucion (id_institucion, nombre, tipo, ruc, direccion, telefono, email, sitio_web, estado) FROM stdin;
1	Oficina de Normalización Previsional - ONP	Pensiones	20131370659	Av. Arenales 1202, Lima	01-5905555	consultas@onp.gob.pe	https://www.onp.gob.pe	Activo
2	AFP Integra	Financiera	20100256130	Av. Javier Prado Este 5266, Lima	01-6170000	atencion@integra.com.pe	https://www.integra.com.pe	Activo
3	AFP Prima	Financiera	20346199679	Av. Javier Prado Este 568, Lima	01-5117000	contacto@prima.com.pe	https://www.prima.com.pe	Activo
4	AFP Habitat	Financiera	20509505465	Av. Víctor Andrés Belaúnde 147, Lima	01-5005500	servicios@afphabitat.com.pe	https://www.afphabitat.com.pe	Activo
5	AFP Profuturo	Financiera	20100147898	Jr. Cusco 141, Lima	01-6154100	atencionalafiliado@profuturo.com.pe	https://www.profuturo.com.pe	Activo
\.


--
-- TOC entry 4657 (class 0 OID 17841)
-- Dependencies: 239
-- Data for Name: notificacion; Type: TABLE DATA; Schema: public; Owner: -
--

COPY public.notificacion (id_notificacion, id_usuario, tipo_notificacion, mensaje, fecha_envio, fecha_lectura, canal, prioridad, estado) FROM stdin;
\.


--
-- TOC entry 4659 (class 0 OID 17849)
-- Dependencies: 241
-- Data for Name: pago_seguro; Type: TABLE DATA; Schema: public; Owner: -
--

COPY public.pago_seguro (id_pago, id_seguro, numero_cuota, monto_pagado, fecha_pago, metodo_pago, estado, fecha_vencimiento, monto_cuota, observaciones) FROM stdin;
1	1	1	200.00	2025-01-05 08:00:00	Débito automático	Pagado	\N	\N	\N
2	1	2	200.00	2025-02-05 08:00:00	Débito automático	Pagado	\N	\N	\N
3	1	3	200.00	2025-03-05 08:00:00	Débito automático	Pagado	\N	\N	\N
4	1	4	200.00	2025-04-05 08:00:00	Débito automático	Pagado	\N	\N	\N
5	2	1	300.00	2025-01-10 10:30:00	Tarjeta de crédito	Pagado	\N	\N	\N
6	2	2	300.00	2025-02-10 10:30:00	Tarjeta de crédito	Pagado	\N	\N	\N
7	2	3	300.00	2025-03-10 10:30:00	Tarjeta de crédito	Pagado	\N	\N	\N
8	6	4	300.00	\N	Débito automático	Pendiente	\N	\N	\N
9	8	1	150.00	2025-01-15 08:00:00	Débito automático	Pagado	\N	\N	\N
10	8	2	150.00	2025-02-15 08:00:00	Débito automático	Pagado	\N	\N	\N
11	8	3	150.00	2025-03-15 08:00:00	Débito automático	Pagado	\N	\N	\N
13	20	2	\N	\N	\N	Pendiente	2025-11-26 23:59:59	1000.00	Cuota generada automáticamente
14	20	3	\N	\N	\N	Pendiente	2025-12-26 23:59:59	1000.00	Cuota generada automáticamente
15	20	4	\N	\N	\N	Pendiente	2026-01-26 23:59:59	1000.00	Cuota generada automáticamente
17	22	2	\N	\N	\N	Pendiente	2025-11-26 23:59:59	1000.00	Cuota generada automáticamente
18	22	3	\N	\N	\N	Pendiente	2025-12-26 23:59:59	1000.00	Cuota generada automáticamente
19	22	4	\N	\N	\N	Pendiente	2026-01-26 23:59:59	1000.00	Cuota generada automáticamente
20	22	5	\N	\N	\N	Pendiente	2026-02-26 23:59:59	1000.00	Cuota generada automáticamente
21	22	6	\N	\N	\N	Pendiente	2026-03-26 23:59:59	1000.00	Cuota generada automáticamente
22	22	7	\N	\N	\N	Pendiente	2026-04-26 23:59:59	1000.00	Cuota generada automáticamente
23	22	8	\N	\N	\N	Pendiente	2026-05-26 23:59:59	1000.00	Cuota generada automáticamente
24	22	9	\N	\N	\N	Pendiente	2026-06-26 23:59:59	1000.00	Cuota generada automáticamente
25	22	10	\N	\N	\N	Pendiente	2026-07-26 23:59:59	1000.00	Cuota generada automáticamente
26	22	11	\N	\N	\N	Pendiente	2026-08-26 23:59:59	1000.00	Cuota generada automáticamente
27	22	12	\N	\N	\N	Pendiente	2026-09-26 23:59:59	1000.00	Cuota generada automáticamente
28	22	13	\N	\N	\N	Pendiente	2026-10-26 23:59:59	1000.00	Cuota generada automáticamente
12	20	1	1000.00	2025-10-27 09:23:03.706	Tarjeta de crédito	Pagado	2025-10-26 23:59:59	1000.00	\N
16	22	1	1000.00	2025-10-27 04:28:31.08452	Tarjeta de débito	Pagado	2025-10-26 23:59:59	1000.00	Cuota generada automáticamente
29	23	1	10000.00	2025-10-27 08:48:47.876114	Tarjeta de crédito	Pagado	2025-10-26 23:59:59	10000.00	Cuota generada automáticamente
30	24	1	\N	\N	\N	Pendiente	2025-10-26 23:59:59	24000.00	Cuota generada automáticamente
\.


--
-- TOC entry 4661 (class 0 OID 17856)
-- Dependencies: 243
-- Data for Name: rol_usuario; Type: TABLE DATA; Schema: public; Owner: -
--

COPY public.rol_usuario (id_rol, nombre_rol, descripcion, permisos, estado) FROM stdin;
1	Administrador	Acceso total al sistema, gestión de usuarios y configuración	{"aportes": "crud", "seguros": "crud", "reportes": "read", "usuarios": "crud", "configuracion": "crud"}	Activo
2	Usuario	Usuario estándar con acceso a consultas personales	{"perfil": "read-update", "aportes": "read", "seguros": "read", "consultas": "create"}	Activo
3	Asesor	Asesor financiero con acceso a múltiples usuarios	{"clientes": "read", "reportes": "read", "asesoramiento": "crud"}	Activo
\.


--
-- TOC entry 4663 (class 0 OID 17865)
-- Dependencies: 245
-- Data for Name: saldo_pension; Type: TABLE DATA; Schema: public; Owner: -
--

COPY public.saldo_pension (id_saldo, id_usuario, id_afp, id_tipo_fondo, saldo_total, saldo_disponible, saldo_cic, saldo_cv, rentabilidad_acumulada, fecha_corte, fecha_actualizacion, estado) FROM stdin;
38	12	\N	2	1000.00	900.00	600.00	400.00	50.00	2025-10-27	2025-10-27 00:00:00	Activo
36	1	2	2	11202.99	10082.69	6721.79	4481.20	560.15	2025-10-28	2025-10-28 00:00:00	Activo
37	6	\N	2	7722.00	6949.80	4633.20	3088.80	386.10	2025-11-27	2025-11-27 00:00:00	Activo
\.


--
-- TOC entry 4665 (class 0 OID 17873)
-- Dependencies: 247
-- Data for Name: seguro; Type: TABLE DATA; Schema: public; Owner: -
--

COPY public.seguro (id_seguro, id_usuario, id_tipo_seguro, id_compania, numero_poliza, numero_contrato, fecha_inicio, fecha_vencimiento, monto_asegurado, prima_mensual, prima_anual, deducible, beneficiarios, coberturas, exclusiones, forma_pago, metodo_pago, fecha_registro, estado, dias_gracia, alerta_vencimiento) FROM stdin;
1	1	1	1	POL-RIMAC-AUTO-001	CONT-001	2025-01-01	2025-12-31	150000.00	200.00	2400.00	500.00	[]	["Responsabilidad civil", "Robo total", "Daños a terceros"]	["Conducción bajo influencia", "Eventos catastróficos no declarados"]	Mensual	Débito automático	2025-01-01 00:00:00	Activo	15	t
2	1	2	2	POL-PACIF-AUTO-001	CONT-002	2025-01-01	2025-12-31	250000.00	300.00	3600.00	300.00	[]	["Cobertura completa", "Grúa 24/7", "Auto de reemplazo", "Asistencia médica ocupantes"]	["Uso comercial no declarado", "Modificaciones no autorizadas"]	Mensual	Tarjeta de crédito	2025-01-01 00:00:00	Activo	15	t
3	1	3	3	POL-MAPFR-AUTO-001	CONT-003	2025-01-01	2025-12-31	300000.00	350.00	4200.00	400.00	[]	["Todo riesgo", "Asistencia médica", "Defensa legal", "Pérdida total", "Eventos naturales"]	["Carreras o competencias", "Transporte de mercancías peligrosas"]	Mensual	Débito automático	2025-01-01 00:00:00	Activo	15	t
4	1	4	1	POL-RIMAC-HOME-001	CONT-004	2025-01-01	2025-12-31	500000.00	150.00	1800.00	300.00	[]	["Cobertura total estructura", "Responsabilidad civil", "Gastos adicionales", "Robo y asalto"]	["Daños por guerra", "Actos intencionales"]	Mensual	Débito automático	2025-01-01 00:00:00	Activo	15	t
5	1	5	2	POL-PACIF-HOME-001	CONT-005	2025-01-01	2025-12-31	650000.00	200.00	2400.00	200.00	[]	["Todo incluido", "Valuables hasta S/50,000", "Hospedaje temporal", "Mascotas", "Electrodomésticos"]	["Desgaste natural", "Mantenimiento inadecuado"]	Mensual	Tarjeta de crédito	2025-01-01 00:00:00	Activo	15	t
6	1	6	3	POL-MAPFR-HEAL-001	CONT-006	2025-01-01	2025-12-31	30000.00	300.00	3600.00	200.00	[]	["Consultas médicas", "Medicinas", "Emergencias", "Hospitalización básica"]	["Enfermedades preexistentes no declaradas", "Tratamientos estéticos"]	Mensual	Débito automático	2025-01-01 00:00:00	Activo	15	t
7	1	7	2	POL-PACIF-HEAL-001	CONT-007	2025-01-01	2025-12-31	80000.00	500.00	6000.00	150.00	[]	["Cobertura internacional", "Medicina preventiva", "Dental completo", "Oncología", "Maternidad"]	["Cirugías estéticas electivas", "Tratamientos experimentales"]	Mensual	Tarjeta de crédito	2025-01-01 00:00:00	Activo	15	t
8	1	8	1	POL-RIMAC-LIFE-001	CONT-008	2025-01-01	2025-12-31	250000.00	150.00	1800.00	\N	[{"nombre": "María García", "parentesco": "Cónyuge", "porcentaje": 100}]	["Fallecimiento natural", "Fallecimiento accidental", "Gastos funerarios hasta S/10,000"]	["Suicidio en primer año", "Deportes extremos no declarados"]	Mensual	Débito automático	2025-01-01 00:00:00	Activo	15	t
9	1	9	3	POL-MAPFR-LIFE-001	CONT-009	2025-01-01	2025-12-31	500000.00	270.00	3240.00	\N	[{"nombre": "María García", "parentesco": "Cónyuge", "porcentaje": 60}, {"nombre": "Juan Mendoza Jr.", "parentesco": "Hijo", "porcentaje": 40}]	["Cobertura internacional", "Enfermedades graves", "Invalidez total y permanente", "Gastos funerarios hasta S/20,000", "Adelanto por enfermedad terminal"]	["Participación en actos delictivos", "Enfermedades preexistentes no declaradas"]	Mensual	Tarjeta de crédito	2025-01-01 00:00:00	Activo	15	t
10	1	1	1	POL-RIMAC-AUTO-002	CONT-010	2025-01-15	2026-01-14	180000.00	220.00	2640.00	600.00	[]	["Responsabilidad civil ampliada", "Robo total y parcial", "Asistencia en carretera 24/7", "Auto de reemplazo 15 días"]	["Conducción en estado de ebriedad", "Modificaciones no autorizadas al vehículo"]	Mensual	Débito automático	2025-10-25 02:50:00.00769	Activo	15	t
11	1	11	2	POL-PACIF-HOME-002	CONT-011	2025-02-01	2026-01-31	400000.00	130.00	1560.00	250.00	[]	["Incendio", "Robo", "Responsabilidad civil", "Daños por agua"]	["Desgaste natural", "Falta de mantenimiento"]	Mensual	Tarjeta de crédito	2025-10-25 02:50:00.00769	Activo	15	t
12	1	12	1	POL-RIMAC-HEAL-001	CONT-012	2025-01-01	2025-12-31	20000.00	150.00	1800.00	150.00	[]	["Consultas médicas", "Medicinas", "Emergencias", "Análisis de laboratorio básicos"]	["Enfermedades preexistentes", "Tratamientos estéticos"]	Mensual	Débito automático	2025-10-25 02:50:00.00769	Activo	15	t
13	1	13	2	POL-PACIF-HEAL-002	CONT-013	2025-03-01	2026-02-28	100000.00	450.00	5400.00	200.00	[{"edad": 38, "nombre": "María García", "parentesco": "Esposa"}, {"edad": 12, "nombre": "Juan Mendoza Jr.", "parentesco": "Hijo"}]	["Consultas ilimitadas", "Hospitalización", "Maternidad", "Pediatría", "Dental", "Oftalmología"]	["Cirugías estéticas", "Tratamientos experimentales"]	Mensual	Tarjeta de crédito	2025-10-25 02:50:00.00769	Activo	15	t
14	1	15	1	POL-RIMAC-LIFE-002	CONT-014	2025-01-01	2030-12-31	300000.00	180.00	2160.00	\N	[{"nombre": "María García", "parentesco": "Cónyuge", "porcentaje": 100}]	["Fallecimiento por cualquier causa", "Invalidez total y permanente", "Gastos funerarios S/15,000"]	["Suicidio primer año", "Deportes extremos sin declarar"]	Mensual	Débito automático	2025-10-25 02:50:00.00769	Activo	15	t
15	2	12	1	POL-RIMAC-HEAL-002	CONT-020	2025-01-01	2025-12-31	25000.00	160.00	1920.00	200.00	[]	["Consultas médicas", "Medicinas", "Emergencias", "Análisis básicos"]	["Enfermedades preexistentes", "Cirugías estéticas"]	Mensual	Débito automático	2025-10-25 02:50:00.00769	Activo	15	t
16	2	8	2	POL-PACIF-LIFE-002	CONT-021	2024-06-01	2029-05-31	200000.00	140.00	1680.00	\N	[{"nombre": "Pedro García", "parentesco": "Padre", "porcentaje": 100}]	["Fallecimiento natural", "Fallecimiento accidental", "Gastos funerarios S/12,000"]	["Suicidio primer año"]	Mensual	Tarjeta de crédito	2025-10-25 02:50:00.00769	Activo	15	t
18	6	2	1	POL-2025-2	\N	2025-10-13	2025-10-07	5999.00	777.00	\N	\N	\N	\N	\N	Semestral	\N	2025-10-26 19:12:05.839	Vigente	15	t
20	6	2	1	POL-2025-11	\N	2025-10-26	2026-02-11	10000.00	1000.00	\N	\N	\N	\N	\N	Mensual	\N	2025-10-27 03:42:09.289	Vigente	15	t
22	6	1	1	POL-20251027-034645	\N	2025-10-26	2026-10-26	10000.00	1000.00	\N	\N	\N	\N	\N	Mensual	\N	2025-10-27 03:47:08.227	Vigente	15	t
17	6	2	1	POL-2025-1	\N	2025-10-06	2025-10-27	5000.00	150.00	\N	\N	\N	\N	\N	Mensual	\N	\N	Activo	15	t
23	12	12	3	POL-20251027-084549	\N	2025-10-25	2025-10-25	100000.00	10000.00	\N	\N	\N	\N	\N	Mensual	\N	\N	Activo	15	t
24	12	1	1	POL-20251027-085504	\N	2025-10-25	2025-10-28	20000.00	2000.00	\N	\N	\N	\N	\N	Anual	\N	\N	Activo	15	t
\.


--
-- TOC entry 4667 (class 0 OID 17885)
-- Dependencies: 249
-- Data for Name: tipo_fondo; Type: TABLE DATA; Schema: public; Owner: -
--

COPY public.tipo_fondo (id_tipo_fondo, nombre_tipo, descripcion, categoria, rentabilidad_esperada, estado) FROM stdin;
1	ONP	Fondo de la Oficina de Normalización Previsional	Pensiones	0.00	Activo
2	AFP Fondo 0	Fondo de preservación de capital - Muy Conservador	Inversión	3.50	Activo
3	AFP Fondo 1	Fondo de protección de capital - Conservador	Inversión	5.20	Activo
4	AFP Fondo 2	Fondo balanceado - Moderado	Inversión	7.80	Activo
5	AFP Fondo 3	Fondo de crecimiento - Agresivo	Inversión	10.50	Activo
\.


--
-- TOC entry 4669 (class 0 OID 17894)
-- Dependencies: 251
-- Data for Name: tipo_seguro; Type: TABLE DATA; Schema: public; Owner: -
--

COPY public.tipo_seguro (id_tipo_seguro, nombre, descripcion, categoria, cobertura_principal, estado) FROM stdin;
1	Seguro Vehicular Básico	Cobertura básica para vehículos particulares	Vehicular	Responsabilidad civil y robo total	Activo
2	Seguro Vehicular Premium	Cobertura completa para vehículos de alta gama	Vehicular	Todo riesgo, asistencia 24/7, auto de reemplazo	Activo
3	Seguro Vehicular Total	Máxima protección vehicular con coberturas extendidas	Vehicular	Todo riesgo plus, defensa legal, asistencia médica	Activo
4	Seguro Hogar Completo	Protección integral para tu hogar	Hogar	Estructura, contenido y responsabilidad civil	Activo
5	Seguro Hogar Premium	Cobertura ampliada con servicios adicionales	Hogar	Todo incluido, valuables, hospedaje temporal	Activo
6	Seguro Salud Básico	Cobertura médica esencial	Salud	Consultas, emergencias y medicinas básicas	Activo
7	Seguro Salud Premium	Cobertura médica completa internacional	Salud	Cobertura internacional, medicina preventiva, dental	Activo
8	Seguro Vida Clásico	Protección familiar básica	Vida	Fallecimiento natural, accidental y gastos funerarios	Activo
9	Seguro Vida Premium	Máxima protección para tu familia	Vida	Cobertura internacional, enfermedades graves, invalidez total	Activo
10	Seguro Vehicular Básico Plus	Cobertura básica mejorada con asistencia en viaje	Vehicular	Responsabilidad civil, robo total y asistencia en carretera	Activo
11	Seguro Vehicular Comercial	Para vehículos de uso comercial y transporte	Vehicular	Todo riesgo comercial, carga y mercancías	Activo
12	Seguro Hogar Básico	Protección esencial para tu hogar	Hogar	Incendio, robo y responsabilidad civil básica	Activo
13	Seguro Hogar Todo Incluido	Máxima protección para tu patrimonio	Hogar	Cobertura total: estructura, contenido, fenómenos naturales	Activo
14	Seguro Salud Económico	Plan básico de salud para consultas y emergencias	Salud	Consultas, medicinas básicas y emergencias	Activo
15	Seguro Salud Familiar	Cobertura completa para toda la familia	Salud	Consultas, hospitalización, maternidad y pediatría	Activo
16	Seguro Salud Internacional	Cobertura médica mundial para viajeros	Salud	Atención médica internacional, evacuación y repatriación	Activo
17	Seguro Vida Temporal	Protección por un periodo determinado	Vida	Fallecimiento durante el periodo contratado	Activo
18	Seguro Vida Dotal	Ahorro y protección combinados	Vida	Fallecimiento o supervivencia al término del contrato	Activo
\.


--
-- TOC entry 4674 (class 0 OID 24587)
-- Dependencies: 256
-- Data for Name: tramite_seguro; Type: TABLE DATA; Schema: public; Owner: -
--

COPY public.tramite_seguro (id_tramite, id_usuario, id_seguro, tipo_tramite, descripcion, fecha_solicitud, fecha_resolucion, estado, respuesta, documentos_adjuntos, prioridad, canal_atencion) FROM stdin;
1	1	1	Reclamo	Demora en el reembolso de gastos médicos por consulta especializada del 15/01/2025. Adjunto facturas y recetas.	2025-01-20 10:30:00	\N	En proceso	\N	\N	Alta	Web
2	1	2	Solicitud	Solicitud de inclusión de beneficiario adicional (hijo menor) en póliza vehicular POL-PACIF-AUTO-001	2024-12-15 14:20:00	2024-12-20 09:15:00	Resuelto	Solicitud aprobada. Beneficiario incluido a partir del 01/01/2025. Prima actualizada.	\N	Media	Teléfono
3	1	4	Consulta	Consulta sobre cobertura para remodelación de cocina por daños en instalaciones eléctricas	2025-01-10 16:45:00	2025-01-11 10:00:00	Resuelto	La póliza cubre daños eléctricos súbitos e imprevistos. Cobertura máxima S/50,000. Requiere peritaje previo.	\N	Baja	Email
4	1	3	Renovación	Solicitud de renovación anticipada de póliza POL-MAPFR-AUTO-001 con mejora de cobertura	2025-01-25 11:00:00	\N	Pendiente	\N	\N	Media	Web
5	1	6	Reclamo	Solicitud de reembolso por consulta con especialista no afiliado a la red	2025-01-05 09:30:00	2025-01-08 15:20:00	Rechazado	Lamentamos informar que el reembolso no procede ya que la consulta fue realizada fuera de la red de prestadores afiliados. Revisar cláusula 5.2 del contrato.	\N	Media	Presencial
6	6	17	Reclamo	Prueba	2025-10-26 19:28:32.595798	\N	Pendiente	\N	\N	Baja	Web
\.


--
-- TOC entry 4671 (class 0 OID 17903)
-- Dependencies: 253
-- Data for Name: usuario; Type: TABLE DATA; Schema: public; Owner: -
--

COPY public.usuario (id_usuario, id_rol, nombre, apellido, dni, fecha_nacimiento, genero, estado_civil, correo, telefono, direccion, distrito, provincia, departamento, contrasena, clave_sol, centro_trabajo, salario_actual, fecha_ingreso_trabajo, tipo_contrato, cuspp, id_afp, tipo_regimen, fecha_afiliacion, notificaciones_email, notificaciones_sms, estado, ultimo_acceso, foto_perfil) FROM stdin;
1	1	Carlos	Mendoza	12345678	1985-03-12	M	Soltero	carlos.mendoza@admin.com	987654321	Av. Principal 123	Cercado	Arequipa	Arequipa	1234	1234	Empresa Administradora SAC	6000.00	2015-01-07	Dependiente	CUSPP001234567890	4	SPP	2015-01-29	t	t	Activo	2025-10-14 22:48:55.304915	data:image/png;base64,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
8	2	Jhon	Belaunde	123456789	2025-10-01	M	Casado	jhon123@gmail.com	44444444	Siempre viva 101	Cercado	Arequipa	Arequipa	\N	1234	Bcp	\N	2025-10-08	Independiente		\N	ONP	2025-10-17	t	t	Inactivo	\N	\N
10	2	Juan	Cuentas	98787656	2002-06-11	M	Soltero	juan123@hotmail.com	876787662	Av. Parra	Cercado	Arequipa	Arequipa	\N	1234	UTP	\N	2025-10-28	Dependiente	\N	\N	SPP	2025-10-28	t	t	Activo	\N	\N
9	2	Prueba	Smith	76869534	2001-01-16	M	Casado	prueba1@hotmail.com	987656422	Tacna 1234				\N	1234		\N	\N			\N		\N	f	f	Inactivo	\N	\N
11	2	Antony	Lujan	78667744	2025-10-08	M	Soltero	antony123@gmail.com	965678987	Las palmera 107	Hunter	Arequipa	Arequipa	\N	1234	TOTTU SAC	\N	2025-10-25	Dependiente	\N	\N	SPP	2025-06-27	t	f	Inactivo	\N	\N
12	2	Jhon	Prueba	11111111	2015-01-20	M	Soltero	jhonprueba@gmail.com	987876672	Av. tacna 2003	Cercado	Arequipa	Arequipa	\N	1234	TOTTU SAC	\N	2025-10-27	Independiente	\N	2	SPP	2025-10-29	t	f	Activo	\N	\N
6	2	Orlando	Ramos	60967428	2025-09-25	M	Soltero	orlando@oficial.com	36521485	Alto de la alizan 102	Paucarpata	Arequipa	Arequipa	\N	12345	Utp	\N	2025-09-24	Dependiente	CUSPP001234567890	4	SPP	2025-09-27	t	t	Activo	\N	\N
3	2	Luis	Ramírez	45678912	1988-11-30	M	Casado	luis.ramirez@email.com	923456789	Jr. Independencia 789	Cayma	Arequipa	Arequipa	1234	1234	Servicios Tecnológicos SAC	4500.00	2018-03-20	Dependiente	CUSPP005551112222	\N	SPP	2018-04-01	t	t	Activo	2025-10-14 22:48:55.304915	\N
5	2	Juan	Perez	67483934	1990-01-01	M	Soltero	juan@mail.com	987654321	Av. Siempre Viva 123	Miraflores	Lima	Lima	\N	1234	Sumaq Seguros	\N	2020-01-01	Dependiente	\N	\N	ONP	2020-02-01	t	f	Activo	\N	\N
7	2	Pipito	Luque	789456123	2025-10-08	M	Soltero	pipito123@gmail.com	52698745	Calle argentina 123	Miraflores	Arequipa	Arequipa	\N	1234	bbva	\N	2025-10-13	Independiente	\N	\N	SPP	2025-09-30	t	f	Activo	\N	\N
2	2	María	García	87654321	1990-07-22	F	Soltera	maria.garcia@email.com	965432187	Calle Los Rosales 456	Yanahuara	Arequipa	Arequipa	1234	1234	Comercial del Sur EIRL	3200.00	2020-06-15	Dependiente	CUSPP009876543210	\N	SPP	2020-07-01	t	f	Activo	2025-10-14 22:48:55.304915	\N
\.


--
-- TOC entry 4703 (class 0 OID 0)
-- Dependencies: 220
-- Name: administrador_id_admin_seq; Type: SEQUENCE SET; Schema: public; Owner: -
--

SELECT pg_catalog.setval('public.administrador_id_admin_seq', 1, false);


--
-- TOC entry 4704 (class 0 OID 0)
-- Dependencies: 222
-- Name: afp_id_afp_seq; Type: SEQUENCE SET; Schema: public; Owner: -
--

SELECT pg_catalog.setval('public.afp_id_afp_seq', 4, true);


--
-- TOC entry 4705 (class 0 OID 0)
-- Dependencies: 224
-- Name: aporte_pension_id_aporte_seq; Type: SEQUENCE SET; Schema: public; Owner: -
--

SELECT pg_catalog.setval('public.aporte_pension_id_aporte_seq', 22, true);


--
-- TOC entry 4706 (class 0 OID 0)
-- Dependencies: 226
-- Name: asesor_id_asesor_seq; Type: SEQUENCE SET; Schema: public; Owner: -
--

SELECT pg_catalog.setval('public.asesor_id_asesor_seq', 1, false);


--
-- TOC entry 4707 (class 0 OID 0)
-- Dependencies: 228
-- Name: asesoramiento_financiero_id_asesoramiento_seq; Type: SEQUENCE SET; Schema: public; Owner: -
--

SELECT pg_catalog.setval('public.asesoramiento_financiero_id_asesoramiento_seq', 1, false);


--
-- TOC entry 4708 (class 0 OID 0)
-- Dependencies: 230
-- Name: autenticacion_id_autenticacion_seq; Type: SEQUENCE SET; Schema: public; Owner: -
--

SELECT pg_catalog.setval('public.autenticacion_id_autenticacion_seq', 1, false);


--
-- TOC entry 4709 (class 0 OID 0)
-- Dependencies: 257
-- Name: beneficiario_seguro_id_beneficiario_seq; Type: SEQUENCE SET; Schema: public; Owner: -
--

SELECT pg_catalog.setval('public.beneficiario_seguro_id_beneficiario_seq', 5, true);


--
-- TOC entry 4710 (class 0 OID 0)
-- Dependencies: 232
-- Name: compania_seguro_id_compania_seq; Type: SEQUENCE SET; Schema: public; Owner: -
--

SELECT pg_catalog.setval('public.compania_seguro_id_compania_seq', 8, true);


--
-- TOC entry 4711 (class 0 OID 0)
-- Dependencies: 234
-- Name: historial_consultas_id_historial_seq; Type: SEQUENCE SET; Schema: public; Owner: -
--

SELECT pg_catalog.setval('public.historial_consultas_id_historial_seq', 62, true);


--
-- TOC entry 4712 (class 0 OID 0)
-- Dependencies: 236
-- Name: historial_rentabilidad_id_rentabilidad_seq; Type: SEQUENCE SET; Schema: public; Owner: -
--

SELECT pg_catalog.setval('public.historial_rentabilidad_id_rentabilidad_seq', 1, true);


--
-- TOC entry 4713 (class 0 OID 0)
-- Dependencies: 238
-- Name: institucion_id_institucion_seq; Type: SEQUENCE SET; Schema: public; Owner: -
--

SELECT pg_catalog.setval('public.institucion_id_institucion_seq', 5, true);


--
-- TOC entry 4714 (class 0 OID 0)
-- Dependencies: 240
-- Name: notificacion_id_notificacion_seq; Type: SEQUENCE SET; Schema: public; Owner: -
--

SELECT pg_catalog.setval('public.notificacion_id_notificacion_seq', 1, false);


--
-- TOC entry 4715 (class 0 OID 0)
-- Dependencies: 242
-- Name: pago_seguro_id_pago_seq; Type: SEQUENCE SET; Schema: public; Owner: -
--

SELECT pg_catalog.setval('public.pago_seguro_id_pago_seq', 30, true);


--
-- TOC entry 4716 (class 0 OID 0)
-- Dependencies: 244
-- Name: rol_usuario_id_rol_seq; Type: SEQUENCE SET; Schema: public; Owner: -
--

SELECT pg_catalog.setval('public.rol_usuario_id_rol_seq', 1, false);


--
-- TOC entry 4717 (class 0 OID 0)
-- Dependencies: 246
-- Name: saldo_pension_id_saldo_seq; Type: SEQUENCE SET; Schema: public; Owner: -
--

SELECT pg_catalog.setval('public.saldo_pension_id_saldo_seq', 38, true);


--
-- TOC entry 4718 (class 0 OID 0)
-- Dependencies: 248
-- Name: seguro_id_seguro_seq; Type: SEQUENCE SET; Schema: public; Owner: -
--

SELECT pg_catalog.setval('public.seguro_id_seguro_seq', 24, true);


--
-- TOC entry 4719 (class 0 OID 0)
-- Dependencies: 250
-- Name: tipo_fondo_id_tipo_fondo_seq; Type: SEQUENCE SET; Schema: public; Owner: -
--

SELECT pg_catalog.setval('public.tipo_fondo_id_tipo_fondo_seq', 5, true);


--
-- TOC entry 4720 (class 0 OID 0)
-- Dependencies: 252
-- Name: tipo_seguro_id_tipo_seguro_seq; Type: SEQUENCE SET; Schema: public; Owner: -
--

SELECT pg_catalog.setval('public.tipo_seguro_id_tipo_seguro_seq', 18, true);


--
-- TOC entry 4721 (class 0 OID 0)
-- Dependencies: 255
-- Name: tramite_seguro_id_tramite_seq; Type: SEQUENCE SET; Schema: public; Owner: -
--

SELECT pg_catalog.setval('public.tramite_seguro_id_tramite_seq', 6, true);


--
-- TOC entry 4722 (class 0 OID 0)
-- Dependencies: 254
-- Name: usuario_id_usuario_seq; Type: SEQUENCE SET; Schema: public; Owner: -
--

SELECT pg_catalog.setval('public.usuario_id_usuario_seq', 12, true);


--
-- TOC entry 4414 (class 2606 OID 17937)
-- Name: administrador administrador_pkey; Type: CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.administrador
    ADD CONSTRAINT administrador_pkey PRIMARY KEY (id_admin);


--
-- TOC entry 4416 (class 2606 OID 17939)
-- Name: afp afp_pkey; Type: CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.afp
    ADD CONSTRAINT afp_pkey PRIMARY KEY (id_afp);


--
-- TOC entry 4418 (class 2606 OID 17941)
-- Name: aporte_pension aporte_pension_pkey; Type: CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.aporte_pension
    ADD CONSTRAINT aporte_pension_pkey PRIMARY KEY (id_aporte);


--
-- TOC entry 4420 (class 2606 OID 17943)
-- Name: asesor asesor_codigo_asesor_key; Type: CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.asesor
    ADD CONSTRAINT asesor_codigo_asesor_key UNIQUE (codigo_asesor);


--
-- TOC entry 4422 (class 2606 OID 17945)
-- Name: asesor asesor_pkey; Type: CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.asesor
    ADD CONSTRAINT asesor_pkey PRIMARY KEY (id_asesor);


--
-- TOC entry 4424 (class 2606 OID 17947)
-- Name: asesoramiento_financiero asesoramiento_financiero_pkey; Type: CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.asesoramiento_financiero
    ADD CONSTRAINT asesoramiento_financiero_pkey PRIMARY KEY (id_asesoramiento);


--
-- TOC entry 4426 (class 2606 OID 17949)
-- Name: autenticacion autenticacion_pkey; Type: CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.autenticacion
    ADD CONSTRAINT autenticacion_pkey PRIMARY KEY (id_autenticacion);


--
-- TOC entry 4462 (class 2606 OID 24628)
-- Name: beneficiario_seguro beneficiario_seguro_pkey; Type: CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.beneficiario_seguro
    ADD CONSTRAINT beneficiario_seguro_pkey PRIMARY KEY (id_beneficiario);


--
-- TOC entry 4428 (class 2606 OID 17951)
-- Name: compania_seguro compania_seguro_pkey; Type: CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.compania_seguro
    ADD CONSTRAINT compania_seguro_pkey PRIMARY KEY (id_compania);


--
-- TOC entry 4430 (class 2606 OID 17955)
-- Name: historial_consultas historial_consultas_pkey; Type: CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.historial_consultas
    ADD CONSTRAINT historial_consultas_pkey PRIMARY KEY (id_historial);


--
-- TOC entry 4432 (class 2606 OID 17957)
-- Name: historial_rentabilidad historial_rentabilidad_pkey; Type: CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.historial_rentabilidad
    ADD CONSTRAINT historial_rentabilidad_pkey PRIMARY KEY (id_rentabilidad);


--
-- TOC entry 4434 (class 2606 OID 17959)
-- Name: institucion institucion_pkey; Type: CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.institucion
    ADD CONSTRAINT institucion_pkey PRIMARY KEY (id_institucion);


--
-- TOC entry 4436 (class 2606 OID 17961)
-- Name: institucion institucion_ruc_key; Type: CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.institucion
    ADD CONSTRAINT institucion_ruc_key UNIQUE (ruc);


--
-- TOC entry 4438 (class 2606 OID 17963)
-- Name: notificacion notificacion_pkey; Type: CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.notificacion
    ADD CONSTRAINT notificacion_pkey PRIMARY KEY (id_notificacion);


--
-- TOC entry 4440 (class 2606 OID 17965)
-- Name: pago_seguro pago_seguro_pkey; Type: CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.pago_seguro
    ADD CONSTRAINT pago_seguro_pkey PRIMARY KEY (id_pago);


--
-- TOC entry 4442 (class 2606 OID 17967)
-- Name: rol_usuario rol_usuario_pkey; Type: CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.rol_usuario
    ADD CONSTRAINT rol_usuario_pkey PRIMARY KEY (id_rol);


--
-- TOC entry 4444 (class 2606 OID 17969)
-- Name: saldo_pension saldo_pension_pkey; Type: CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.saldo_pension
    ADD CONSTRAINT saldo_pension_pkey PRIMARY KEY (id_saldo);


--
-- TOC entry 4446 (class 2606 OID 17971)
-- Name: seguro seguro_numero_poliza_key; Type: CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.seguro
    ADD CONSTRAINT seguro_numero_poliza_key UNIQUE (numero_poliza);


--
-- TOC entry 4448 (class 2606 OID 17973)
-- Name: seguro seguro_pkey; Type: CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.seguro
    ADD CONSTRAINT seguro_pkey PRIMARY KEY (id_seguro);


--
-- TOC entry 4450 (class 2606 OID 17975)
-- Name: tipo_fondo tipo_fondo_pkey; Type: CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.tipo_fondo
    ADD CONSTRAINT tipo_fondo_pkey PRIMARY KEY (id_tipo_fondo);


--
-- TOC entry 4452 (class 2606 OID 17977)
-- Name: tipo_seguro tipo_seguro_pkey; Type: CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.tipo_seguro
    ADD CONSTRAINT tipo_seguro_pkey PRIMARY KEY (id_tipo_seguro);


--
-- TOC entry 4460 (class 2606 OID 24603)
-- Name: tramite_seguro tramite_seguro_pkey; Type: CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.tramite_seguro
    ADD CONSTRAINT tramite_seguro_pkey PRIMARY KEY (id_tramite);


--
-- TOC entry 4454 (class 2606 OID 17979)
-- Name: usuario usuario_correo_key; Type: CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.usuario
    ADD CONSTRAINT usuario_correo_key UNIQUE (correo);


--
-- TOC entry 4456 (class 2606 OID 17981)
-- Name: usuario usuario_dni_key; Type: CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.usuario
    ADD CONSTRAINT usuario_dni_key UNIQUE (dni);


--
-- TOC entry 4458 (class 2606 OID 17983)
-- Name: usuario usuario_pkey; Type: CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.usuario
    ADD CONSTRAINT usuario_pkey PRIMARY KEY (id_usuario);


--
-- TOC entry 4463 (class 2606 OID 17984)
-- Name: administrador administrador_id_usuario_fkey; Type: FK CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.administrador
    ADD CONSTRAINT administrador_id_usuario_fkey FOREIGN KEY (id_usuario) REFERENCES public.usuario(id_usuario);


--
-- TOC entry 4464 (class 2606 OID 17989)
-- Name: afp afp_id_institucion_fkey; Type: FK CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.afp
    ADD CONSTRAINT afp_id_institucion_fkey FOREIGN KEY (id_institucion) REFERENCES public.institucion(id_institucion);


--
-- TOC entry 4465 (class 2606 OID 17994)
-- Name: aporte_pension aporte_pension_id_institucion_fkey; Type: FK CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.aporte_pension
    ADD CONSTRAINT aporte_pension_id_institucion_fkey FOREIGN KEY (id_institucion) REFERENCES public.institucion(id_institucion);


--
-- TOC entry 4466 (class 2606 OID 17999)
-- Name: aporte_pension aporte_pension_id_tipo_fondo_fkey; Type: FK CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.aporte_pension
    ADD CONSTRAINT aporte_pension_id_tipo_fondo_fkey FOREIGN KEY (id_tipo_fondo) REFERENCES public.tipo_fondo(id_tipo_fondo);


--
-- TOC entry 4467 (class 2606 OID 18004)
-- Name: aporte_pension aporte_pension_id_usuario_fkey; Type: FK CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.aporte_pension
    ADD CONSTRAINT aporte_pension_id_usuario_fkey FOREIGN KEY (id_usuario) REFERENCES public.usuario(id_usuario);


--
-- TOC entry 4468 (class 2606 OID 18009)
-- Name: asesor asesor_id_usuario_fkey; Type: FK CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.asesor
    ADD CONSTRAINT asesor_id_usuario_fkey FOREIGN KEY (id_usuario) REFERENCES public.usuario(id_usuario);


--
-- TOC entry 4469 (class 2606 OID 18014)
-- Name: asesoramiento_financiero asesoramiento_financiero_id_aporte_fkey; Type: FK CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.asesoramiento_financiero
    ADD CONSTRAINT asesoramiento_financiero_id_aporte_fkey FOREIGN KEY (id_aporte) REFERENCES public.aporte_pension(id_aporte);


--
-- TOC entry 4470 (class 2606 OID 18019)
-- Name: asesoramiento_financiero asesoramiento_financiero_id_asesor_fkey; Type: FK CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.asesoramiento_financiero
    ADD CONSTRAINT asesoramiento_financiero_id_asesor_fkey FOREIGN KEY (id_asesor) REFERENCES public.asesor(id_asesor);


--
-- TOC entry 4471 (class 2606 OID 18024)
-- Name: asesoramiento_financiero asesoramiento_financiero_id_usuario_fkey; Type: FK CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.asesoramiento_financiero
    ADD CONSTRAINT asesoramiento_financiero_id_usuario_fkey FOREIGN KEY (id_usuario) REFERENCES public.usuario(id_usuario);


--
-- TOC entry 4472 (class 2606 OID 18029)
-- Name: autenticacion autenticacion_id_usuario_fkey; Type: FK CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.autenticacion
    ADD CONSTRAINT autenticacion_id_usuario_fkey FOREIGN KEY (id_usuario) REFERENCES public.usuario(id_usuario);


--
-- TOC entry 4489 (class 2606 OID 24629)
-- Name: beneficiario_seguro beneficiario_seguro_id_seguro_fkey; Type: FK CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.beneficiario_seguro
    ADD CONSTRAINT beneficiario_seguro_id_seguro_fkey FOREIGN KEY (id_seguro) REFERENCES public.seguro(id_seguro);


--
-- TOC entry 4473 (class 2606 OID 18034)
-- Name: compania_seguro compania_seguro_id_institucion_fkey; Type: FK CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.compania_seguro
    ADD CONSTRAINT compania_seguro_id_institucion_fkey FOREIGN KEY (id_institucion) REFERENCES public.institucion(id_institucion);


--
-- TOC entry 4485 (class 2606 OID 18044)
-- Name: usuario fk_usuario_afp; Type: FK CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.usuario
    ADD CONSTRAINT fk_usuario_afp FOREIGN KEY (id_afp) REFERENCES public.afp(id_afp);


--
-- TOC entry 4474 (class 2606 OID 18049)
-- Name: historial_consultas historial_consultas_id_usuario_fkey; Type: FK CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.historial_consultas
    ADD CONSTRAINT historial_consultas_id_usuario_fkey FOREIGN KEY (id_usuario) REFERENCES public.usuario(id_usuario);


--
-- TOC entry 4475 (class 2606 OID 18054)
-- Name: historial_rentabilidad historial_rentabilidad_id_afp_fkey; Type: FK CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.historial_rentabilidad
    ADD CONSTRAINT historial_rentabilidad_id_afp_fkey FOREIGN KEY (id_afp) REFERENCES public.afp(id_afp);


--
-- TOC entry 4476 (class 2606 OID 18059)
-- Name: historial_rentabilidad historial_rentabilidad_id_tipo_fondo_fkey; Type: FK CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.historial_rentabilidad
    ADD CONSTRAINT historial_rentabilidad_id_tipo_fondo_fkey FOREIGN KEY (id_tipo_fondo) REFERENCES public.tipo_fondo(id_tipo_fondo);


--
-- TOC entry 4477 (class 2606 OID 18064)
-- Name: notificacion notificacion_id_usuario_fkey; Type: FK CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.notificacion
    ADD CONSTRAINT notificacion_id_usuario_fkey FOREIGN KEY (id_usuario) REFERENCES public.usuario(id_usuario);


--
-- TOC entry 4478 (class 2606 OID 18069)
-- Name: pago_seguro pago_seguro_id_seguro_fkey; Type: FK CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.pago_seguro
    ADD CONSTRAINT pago_seguro_id_seguro_fkey FOREIGN KEY (id_seguro) REFERENCES public.seguro(id_seguro);


--
-- TOC entry 4479 (class 2606 OID 18074)
-- Name: saldo_pension saldo_pension_id_afp_fkey; Type: FK CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.saldo_pension
    ADD CONSTRAINT saldo_pension_id_afp_fkey FOREIGN KEY (id_afp) REFERENCES public.afp(id_afp);


--
-- TOC entry 4480 (class 2606 OID 18079)
-- Name: saldo_pension saldo_pension_id_tipo_fondo_fkey; Type: FK CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.saldo_pension
    ADD CONSTRAINT saldo_pension_id_tipo_fondo_fkey FOREIGN KEY (id_tipo_fondo) REFERENCES public.tipo_fondo(id_tipo_fondo);


--
-- TOC entry 4481 (class 2606 OID 18084)
-- Name: saldo_pension saldo_pension_id_usuario_fkey; Type: FK CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.saldo_pension
    ADD CONSTRAINT saldo_pension_id_usuario_fkey FOREIGN KEY (id_usuario) REFERENCES public.usuario(id_usuario);


--
-- TOC entry 4482 (class 2606 OID 18089)
-- Name: seguro seguro_id_compania_fkey; Type: FK CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.seguro
    ADD CONSTRAINT seguro_id_compania_fkey FOREIGN KEY (id_compania) REFERENCES public.compania_seguro(id_compania);


--
-- TOC entry 4483 (class 2606 OID 18094)
-- Name: seguro seguro_id_tipo_seguro_fkey; Type: FK CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.seguro
    ADD CONSTRAINT seguro_id_tipo_seguro_fkey FOREIGN KEY (id_tipo_seguro) REFERENCES public.tipo_seguro(id_tipo_seguro);


--
-- TOC entry 4484 (class 2606 OID 18099)
-- Name: seguro seguro_id_usuario_fkey; Type: FK CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.seguro
    ADD CONSTRAINT seguro_id_usuario_fkey FOREIGN KEY (id_usuario) REFERENCES public.usuario(id_usuario);


--
-- TOC entry 4487 (class 2606 OID 24609)
-- Name: tramite_seguro tramite_seguro_id_seguro_fkey; Type: FK CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.tramite_seguro
    ADD CONSTRAINT tramite_seguro_id_seguro_fkey FOREIGN KEY (id_seguro) REFERENCES public.seguro(id_seguro);


--
-- TOC entry 4488 (class 2606 OID 24604)
-- Name: tramite_seguro tramite_seguro_id_usuario_fkey; Type: FK CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.tramite_seguro
    ADD CONSTRAINT tramite_seguro_id_usuario_fkey FOREIGN KEY (id_usuario) REFERENCES public.usuario(id_usuario);


--
-- TOC entry 4486 (class 2606 OID 18104)
-- Name: usuario usuario_id_rol_fkey; Type: FK CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.usuario
    ADD CONSTRAINT usuario_id_rol_fkey FOREIGN KEY (id_rol) REFERENCES public.rol_usuario(id_rol);


-- Completed on 2025-11-27 04:34:28

--
-- PostgreSQL database dump complete
--

\unrestrict mT1IhUEP7WjM3iCKiFMmCtGL5Ha1JkrvqOv822Vfc9K66IRdixF1yu59ldp05jW

