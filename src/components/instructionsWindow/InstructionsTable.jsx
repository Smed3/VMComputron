import SimpleBar from 'simplebar-react';
import styles from './InstructionsTable.module.css';

function InstructionsTable({search=''}) {

    const INSTRUCTIONS = [
        { code: "000", instr: "NOP",  attr: "-"   },
        { code: "001", instr: "BZE",  attr: "adr" },
        { code: "002", instr: "JMP",  attr: "adr" },
        { code: "003", instr: "JSR",  attr: "adr" },
        { code: "004", instr: "RTS",  attr: "-"   },
        { code: "005", instr: "EXIT", attr: "-"   },
        { code: "006", instr: "INPC", attr: "-"   },
        { code: "007", instr: "INP",  attr: "-"   },
        { code: "010", instr: "INPR", attr: "-"   },
        { code: "011", instr: "OUTC", attr: "-"   },
        { code: "012", instr: "OUT",  attr: "-"   },
        { code: "013", instr: "OUTR", attr: "-"   },
        { code: "014", instr: "POP",  attr: "-"   },
        { code: "015", instr: "POPR", attr: "-"   },
        { code: "016", instr: "PUSH", attr: "-"   },
        { code: "017", instr: "PUSHR", attr: "-"   },
        { code: "020", instr: "LDA",   attr: "adr" },
        { code: "021", instr: "LDAM",  attr: "val" },
        { code: "022", instr: "LDA'I", attr: "adr" },
        { code: "023", instr: "LDAX",  attr: "val" },
        { code: "024", instr: "LDR",   attr: "adr" },
        { code: "025", instr: "LDR'I", attr: "adr" },
        { code: "026", instr: "STA",   attr: "adr" },
        { code: "027", instr: "STA'I", attr: "adr" },
        { code: "030", instr: "STR'I", attr: "adr" },
        { code: "031", instr: "LDX",   attr: "adr" },
        { code: "032", instr: "STX",   attr: "adr" },
        { code: "033", instr: "LDS",   attr: "adr" },
        { code: "034", instr: "STS",   attr: "adr" },
        { code: "035", instr: "OR",    attr: "adr" },
        { code: "036", instr: "AND",   attr: "adr" },
        { code: "037", instr: "NOT",   attr: "-"   },
        { code: "040", instr: "EQ",    attr: "adr" },
        { code: "041", instr: "NE",   attr: "adr" },
        { code: "042", instr: "LT",   attr: "adr" },
        { code: "043", instr: "LE",   attr: "adr" },
        { code: "044", instr: "GT",   attr: "adr" },
        { code: "045", instr: "GE",   attr: "adr" },
        { code: "046", instr: "EQR",  attr: "adr" },
        { code: "047", instr: "NER",  attr: "adr" },
        { code: "050", instr: "LTR",  attr: "adr" },
        { code: "051", instr: "LER",  attr: "adr" },
        { code: "052", instr: "GTR",  attr: "adr" },
        { code: "053", instr: "GER",  attr: "adr" },
        { code: "054", instr: "ADD",  attr: "adr" },
        { code: "055", instr: "ADDM", attr: "val" },
        { code: "056", instr: "SUB",  attr: "adr" },
        { code: "057", instr: "SUBM", attr: "val" },
        { code: "060", instr: "MUL",  attr: "adr" },
        { code: "061", instr: "DIV",  attr: "adr" },
        { code: "062", instr: "NEG",  attr: "-"   },
        { code: "063", instr: "ADDR", attr: "adr" },
        { code: "064", instr: "SUBR", attr: "adr" },
        { code: "065", instr: "MULR", attr: "adr" },
        { code: "066", instr: "DIVR", attr: "adr" },
        { code: "067", instr: "NEGR", attr: "adr" },
    ];

    const query = search.trim().toLowerCase();

    const filtered = INSTRUCTIONS.filter(row => 
        row.code.toLowerCase().startsWith(query) ||
        row.instr.toLowerCase().startsWith(query) ||
        row.attr.toLowerCase().startsWith(query)
    );


    return (
        <div className={styles.Table__outer}>
            <SimpleBar 
                className={`${styles.Table__wrapper} sb-scroll table-scroll`}
                autoHide={false}
                scrollbarMaxSize={80}
            >
                <table className={styles.Table}>
                    <thead>
                        <tr>
                            <th>Code</th>
                            <th>Instr</th>
                            <th>Attr</th>
                        </tr>
                    </thead>
                    <tbody>
                        {filtered.map((row, index) => (
                            <tr key={row.code} className={index % 2 !== 0 ? styles.Table__row__dark : ''}>
                                <td>{row.code}</td>
                                <td>{row.instr}</td>
                                <td>{row.attr}</td>
                            </tr>
                        ))}
                    </tbody>
                </table>
            </SimpleBar>
        </div>
    );
}

export default InstructionsTable;