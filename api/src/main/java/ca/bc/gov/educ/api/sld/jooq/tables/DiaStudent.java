/*
 * This file is generated by jOOQ.
 */
package ca.bc.gov.educ.api.sld.jooq.tables;


import ca.bc.gov.educ.api.sld.jooq.DefaultSchema;
import ca.bc.gov.educ.api.sld.jooq.Keys;
import ca.bc.gov.educ.api.sld.jooq.tables.records.DiaStudentRecord;

import java.util.Arrays;
import java.util.List;

import org.jooq.Field;
import org.jooq.ForeignKey;
import org.jooq.Name;
import org.jooq.Record;
import org.jooq.Schema;
import org.jooq.Table;
import org.jooq.TableField;
import org.jooq.TableOptions;
import org.jooq.UniqueKey;
import org.jooq.impl.DSL;
import org.jooq.impl.SQLDataType;
import org.jooq.impl.TableImpl;


/**
 * This class is generated by jOOQ.
 */
@SuppressWarnings({ "all", "unchecked", "rawtypes" })
public class DiaStudent extends TableImpl<DiaStudentRecord> {

    private static final long serialVersionUID = 1L;

    /**
     * The reference instance of <code>DIA_STUDENT</code>
     */
    public static final DiaStudent DIA_STUDENT = new DiaStudent();

    /**
     * The class holding records for this type
     */
    @Override
    public Class<DiaStudentRecord> getRecordType() {
        return DiaStudentRecord.class;
    }

    /**
     * The column <code>DIA_STUDENT.PEN</code>.
     */
    public final TableField<DiaStudentRecord, String> PEN = createField(DSL.name("PEN"), SQLDataType.VARCHAR(255).nullable(false), this, "");

    /**
     * The column <code>DIA_STUDENT.REPORT_DATE</code>.
     */
    public final TableField<DiaStudentRecord, Long> REPORT_DATE = createField(DSL.name("REPORT_DATE"), SQLDataType.BIGINT.nullable(false), this, "");

    /**
     * The column <code>DIA_STUDENT.AGREEMENT_TYPE</code>.
     */
    public final TableField<DiaStudentRecord, String> AGREEMENT_TYPE = createField(DSL.name("AGREEMENT_TYPE"), SQLDataType.VARCHAR(255), this, "");

    /**
     * The column <code>DIA_STUDENT.BAND_CODE</code>.
     */
    public final TableField<DiaStudentRecord, String> BAND_CODE = createField(DSL.name("BAND_CODE"), SQLDataType.VARCHAR(255), this, "");

    /**
     * The column <code>DIA_STUDENT.BANDNAME</code>.
     */
    public final TableField<DiaStudentRecord, String> BANDNAME = createField(DSL.name("BANDNAME"), SQLDataType.VARCHAR(255), this, "");

    /**
     * The column <code>DIA_STUDENT.BANDRESNUM</code>.
     */
    public final TableField<DiaStudentRecord, String> BANDRESNUM = createField(DSL.name("BANDRESNUM"), SQLDataType.VARCHAR(255), this, "");

    /**
     * The column <code>DIA_STUDENT.COMMENT</code>.
     */
    public final TableField<DiaStudentRecord, String> COMMENT = createField(DSL.name("COMMENT"), SQLDataType.VARCHAR(255), this, "");

    /**
     * The column <code>DIA_STUDENT.DIA_SCHOOL_INFO_WRONG</code>.
     */
    public final TableField<DiaStudentRecord, String> DIA_SCHOOL_INFO_WRONG = createField(DSL.name("DIA_SCHOOL_INFO_WRONG"), SQLDataType.VARCHAR(255), this, "");

    /**
     * The column <code>DIA_STUDENT.DISTNO</code>.
     */
    public final TableField<DiaStudentRecord, String> DISTNO = createField(DSL.name("DISTNO"), SQLDataType.VARCHAR(255), this, "");

    /**
     * The column <code>DIA_STUDENT.DISTNO_NEW</code>.
     */
    public final TableField<DiaStudentRecord, String> DISTNO_NEW = createField(DSL.name("DISTNO_NEW"), SQLDataType.VARCHAR(255), this, "");

    /**
     * The column <code>DIA_STUDENT.FRBANDNUM</code>.
     */
    public final TableField<DiaStudentRecord, String> FRBANDNUM = createField(DSL.name("FRBANDNUM"), SQLDataType.VARCHAR(255), this, "");

    /**
     * The column <code>DIA_STUDENT.FTE_VAL</code>.
     */
    public final TableField<DiaStudentRecord, Long> FTE_VAL = createField(DSL.name("FTE_VAL"), SQLDataType.BIGINT, this, "");

    /**
     * The column <code>DIA_STUDENT.ORIG_PEN</code>.
     */
    public final TableField<DiaStudentRecord, String> ORIG_PEN = createField(DSL.name("ORIG_PEN"), SQLDataType.VARCHAR(255), this, "");

    /**
     * The column <code>DIA_STUDENT.PEN_COMMENT</code>.
     */
    public final TableField<DiaStudentRecord, String> PEN_COMMENT = createField(DSL.name("PEN_COMMENT"), SQLDataType.VARCHAR(255), this, "");

    /**
     * The column <code>DIA_STUDENT.PEN_STATUS</code>.
     */
    public final TableField<DiaStudentRecord, String> PEN_STATUS = createField(DSL.name("PEN_STATUS"), SQLDataType.VARCHAR(255), this, "");

    /**
     * The column <code>DIA_STUDENT.POSTED_PEN</code>.
     */
    public final TableField<DiaStudentRecord, String> POSTED_PEN = createField(DSL.name("POSTED_PEN"), SQLDataType.VARCHAR(255), this, "");

    /**
     * The column <code>DIA_STUDENT.RECORD_NUMBER</code>.
     */
    public final TableField<DiaStudentRecord, Long> RECORD_NUMBER = createField(DSL.name("RECORD_NUMBER"), SQLDataType.BIGINT, this, "");

    /**
     * The column <code>DIA_STUDENT.SCHBOARD</code>.
     */
    public final TableField<DiaStudentRecord, String> SCHBOARD = createField(DSL.name("SCHBOARD"), SQLDataType.VARCHAR(255), this, "");

    /**
     * The column <code>DIA_STUDENT.SCHLNO</code>.
     */
    public final TableField<DiaStudentRecord, String> SCHLNO = createField(DSL.name("SCHLNO"), SQLDataType.VARCHAR(255), this, "");

    /**
     * The column <code>DIA_STUDENT.SCHLNO_NEW</code>.
     */
    public final TableField<DiaStudentRecord, String> SCHLNO_NEW = createField(DSL.name("SCHLNO_NEW"), SQLDataType.VARCHAR(255), this, "");

    /**
     * The column <code>DIA_STUDENT.SCHNUM</code>.
     */
    public final TableField<DiaStudentRecord, String> SCHNUM = createField(DSL.name("SCHNUM"), SQLDataType.VARCHAR(255), this, "");

    /**
     * The column <code>DIA_STUDENT.SCHOOL_NAME</code>.
     */
    public final TableField<DiaStudentRecord, String> SCHOOL_NAME = createField(DSL.name("SCHOOL_NAME"), SQLDataType.VARCHAR(255), this, "");

    /**
     * The column <code>DIA_STUDENT.SCHTYPE</code>.
     */
    public final TableField<DiaStudentRecord, String> SCHTYPE = createField(DSL.name("SCHTYPE"), SQLDataType.VARCHAR(255), this, "");

    /**
     * The column <code>DIA_STUDENT.SITENO</code>.
     */
    public final TableField<DiaStudentRecord, String> SITENO = createField(DSL.name("SITENO"), SQLDataType.VARCHAR(255), this, "");

    /**
     * The column <code>DIA_STUDENT.SITENO_NEW</code>.
     */
    public final TableField<DiaStudentRecord, String> SITENO_NEW = createField(DSL.name("SITENO_NEW"), SQLDataType.VARCHAR(255), this, "");

    /**
     * The column <code>DIA_STUDENT.STUD_BIRTH</code>.
     */
    public final TableField<DiaStudentRecord, String> STUD_BIRTH = createField(DSL.name("STUD_BIRTH"), SQLDataType.VARCHAR(255), this, "");

    /**
     * The column <code>DIA_STUDENT.STUD_GIVEN</code>.
     */
    public final TableField<DiaStudentRecord, String> STUD_GIVEN = createField(DSL.name("STUD_GIVEN"), SQLDataType.VARCHAR(255), this, "");

    /**
     * The column <code>DIA_STUDENT.STUD_GRADE</code>.
     */
    public final TableField<DiaStudentRecord, String> STUD_GRADE = createField(DSL.name("STUD_GRADE"), SQLDataType.VARCHAR(255), this, "");

    /**
     * The column <code>DIA_STUDENT.STUD_MIDDLE</code>.
     */
    public final TableField<DiaStudentRecord, String> STUD_MIDDLE = createField(DSL.name("STUD_MIDDLE"), SQLDataType.VARCHAR(255), this, "");

    /**
     * The column <code>DIA_STUDENT.STUD_NEW_FLAG</code>.
     */
    public final TableField<DiaStudentRecord, String> STUD_NEW_FLAG = createField(DSL.name("STUD_NEW_FLAG"), SQLDataType.VARCHAR(255), this, "");

    /**
     * The column <code>DIA_STUDENT.STUD_SEX</code>.
     */
    public final TableField<DiaStudentRecord, String> STUD_SEX = createField(DSL.name("STUD_SEX"), SQLDataType.VARCHAR(255), this, "");

    /**
     * The column <code>DIA_STUDENT.STUD_SURNAME</code>.
     */
    public final TableField<DiaStudentRecord, String> STUD_SURNAME = createField(DSL.name("STUD_SURNAME"), SQLDataType.VARCHAR(255), this, "");

    /**
     * The column <code>DIA_STUDENT.USUAL_GIVEN</code>.
     */
    public final TableField<DiaStudentRecord, String> USUAL_GIVEN = createField(DSL.name("USUAL_GIVEN"), SQLDataType.VARCHAR(255), this, "");

    /**
     * The column <code>DIA_STUDENT.USUAL_SURNAME</code>.
     */
    public final TableField<DiaStudentRecord, String> USUAL_SURNAME = createField(DSL.name("USUAL_SURNAME"), SQLDataType.VARCHAR(255), this, "");

    /**
     * The column <code>DIA_STUDENT.WITHDRAWAL_CODE</code>.
     */
    public final TableField<DiaStudentRecord, String> WITHDRAWAL_CODE = createField(DSL.name("WITHDRAWAL_CODE"), SQLDataType.VARCHAR(255), this, "");

    private DiaStudent(Name alias, Table<DiaStudentRecord> aliased) {
        this(alias, aliased, null);
    }

    private DiaStudent(Name alias, Table<DiaStudentRecord> aliased, Field<?>[] parameters) {
        super(alias, null, aliased, parameters, DSL.comment(""), TableOptions.table());
    }

    /**
     * Create an aliased <code>DIA_STUDENT</code> table reference
     */
    public DiaStudent(String alias) {
        this(DSL.name(alias), DIA_STUDENT);
    }

    /**
     * Create an aliased <code>DIA_STUDENT</code> table reference
     */
    public DiaStudent(Name alias) {
        this(alias, DIA_STUDENT);
    }

    /**
     * Create a <code>DIA_STUDENT</code> table reference
     */
    public DiaStudent() {
        this(DSL.name("DIA_STUDENT"), null);
    }

    public <O extends Record> DiaStudent(Table<O> child, ForeignKey<O, DiaStudentRecord> key) {
        super(child, key, DIA_STUDENT);
    }

    @Override
    public Schema getSchema() {
        return DefaultSchema.DEFAULT_SCHEMA;
    }

    @Override
    public UniqueKey<DiaStudentRecord> getPrimaryKey() {
        return Keys.CONSTRAINT_2;
    }

    @Override
    public List<UniqueKey<DiaStudentRecord>> getKeys() {
        return Arrays.<UniqueKey<DiaStudentRecord>>asList(Keys.CONSTRAINT_2);
    }

    @Override
    public DiaStudent as(String alias) {
        return new DiaStudent(DSL.name(alias), this);
    }

    @Override
    public DiaStudent as(Name alias) {
        return new DiaStudent(alias, this);
    }

    /**
     * Rename this table
     */
    @Override
    public DiaStudent rename(String name) {
        return new DiaStudent(DSL.name(name), null);
    }

    /**
     * Rename this table
     */
    @Override
    public DiaStudent rename(Name name) {
        return new DiaStudent(name, null);
    }
}
