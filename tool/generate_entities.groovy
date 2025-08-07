import com.intellij.database.model.DasTable
import com.intellij.database.util.Case
import com.intellij.database.util.DasUtil

rootPath = "F:\\Code\\POKEMON\\pkm\\src\\main\\java\\com\\phuonghn\\pkm"
subPath = ""

packageName = "com.phuonghn.pkm"
packageSubName = ""
typeMapping = [
        (~/(?i)bigint/)                   : "Long",
        (~/(?i)int/)                      : "Integer",
        (~/(?i)float|double|decimal|real/): "Double",
        (~/(?i)datetime|timestamp/)       : "Date",
        (~/(?i)date/)                     : "Date",
        (~/(?i)time/)                     : "java.sql.Time",
        (~/(?i)/)                         : "String"
]

tableClassName = ""
tableClassObject = ""


// Khai báo các đường dẫn đến các thư mục
pathEntity = rootPath + "\\entity\\" + subPath
pathDTO = rootPath + "\\service\\dto\\" + subPath
pathMapper = rootPath + "\\service\\mapper\\" + subPath
pathRepo = rootPath + "\\repository\\"
pathRepoImpl = rootPath + "\\repository\\impl\\"
pathService = rootPath + "\\service\\"
pathServiceImpl = rootPath + "\\service\\impl\\"
pathController = rootPath + "\\controller\\"

SELECTION.filter { it instanceof DasTable }.each { generate(it) }


def generate(table) {
    tableClassName = javaName(table.getName(), true)
    tableClassObject = javaName(table.getName(), false)
    def fields = calcFields(table)
    println(fields)
    new File(pathEntity + tableClassName + ".java").withPrintWriter { out -> generate(out, tableClassName, fields, table.getName()) }
    def classNameDTO = tableClassName + "DTO"
    new File(pathDTO + classNameDTO + ".java").withPrintWriter { out -> generateDTO(out, classNameDTO, fields) }
    new File(pathMapper + tableClassName + "Mapper.java").withPrintWriter { out -> generateQModel(out, tableClassName, fields, table.getName()) }
    // repo
    def repoJpa = new File(pathRepo + tableClassName.toString().toLowerCase() + "\\" + subPath + tableClassName + "Repo.java")
    def parentDir = repoJpa.getParentFile()
    if (!parentDir.exists()) {
        parentDir.mkdirs() // Tạo thư mục nếu chưa tồn tại
    }
    repoJpa.withPrintWriter { out -> generateRepo(out, tableClassName, fields, table.getName()) }
    //repo custom
    def repoDao = new File(pathRepoImpl + tableClassName.toString().toLowerCase() + "\\" + subPath + tableClassName + "CustomRepo.java")
    def repoDaoDir = repoDao.getParentFile()
    if (!repoDaoDir.exists()) {
        repoDaoDir.mkdirs() // Tạo thư mục nếu chưa tồn tại
    }
    repoDao.withPrintWriter { out -> generateRepoCustom(out, tableClassName, fields, table.getName()) }
    new File(pathRepoImpl + tableClassName.toString().toLowerCase() + "\\" + subPath + tableClassName + "CustomRepoImpl.java").withPrintWriter { out -> generateRepoImpl(out, tableClassName, fields, table.getName()) }
    //Services
    def service = new File(pathService + tableClassName.toString().toLowerCase() + "\\" + subPath + tableClassName + "Service.java")
    def serviceDir = service.getParentFile()
    if (!serviceDir.exists()) {
        serviceDir.mkdirs() // Tạo thư mục nếu chưa tồn tại
    }
    service.withPrintWriter { out -> generateService(out, tableClassName, fields, table.getName()) }
    //Services impl
    def serviceImpl = new File(pathServiceImpl + tableClassName.toString().toLowerCase() + "\\" + subPath + tableClassName + "ServiceImpl.java")
    def serviceImplDir = serviceImpl.getParentFile()
    if (!serviceImplDir.exists()) {
        serviceImplDir.mkdirs() // Tạo thư mục nếu chưa tồn tại
    }
    serviceImpl.withPrintWriter { out -> generateServiceImpl(out, tableClassName, fields, table.getName()) }

    //Controller
    def controller = new File(pathController + tableClassName.toString().toLowerCase() + "\\" + subPath + tableClassName + "Controller.java")
    def controllerDir = controller.getParentFile()
    if (!controllerDir.exists()) {
        controllerDir.mkdirs() // Tạo thư mục nếu chưa tồn tại
    }
    controller.withPrintWriter { out -> generateController(out, tableClassName, fields, table.getName()) }

}

def generate(out, className, fields, tableName) {
    def packageFullName = packageName + ".domain.entities${packageSubName};"
    def excludedFields = ["createdDate", "lastModifiedDate", "createdBy", "lastModifiedBy", "deletedDate", "deletedBy", "deleted"]
    out.print "package $packageFullName"
    out.println ""
    out.println ""
    out.println "import jakarta.persistence.*;"
    out.println "import lombok.AllArgsConstructor;"
    out.println "import lombok.NoArgsConstructor;"
    out.println "import lombok.Getter;"
    out.println "import lombok.Setter;"
    out.println "import java.util.*;"
    out.println ""
    out.println "@Setter"
    out.println "@Getter"
    out.println "@NoArgsConstructor"
    out.println "@AllArgsConstructor"
    out.println "@Entity"
    out.println "@Table(name=\"${tableName}\")"
    out.println "public class $className extends BaseEntity {"
    fields.each() {
        if (!excludedFields.contains(it.name)) {
            if (it.annos != "") out.println "  ${it.annos}"
            if (it.primary) {
                out.println "   @Id"
                out.println("   @GeneratedValue(strategy = GenerationType.IDENTITY)")
            }
            out.println "   @Column(name=\"${Case.UPPER.apply(it.colName)}\")"
            out.println "   private ${it.type} ${it.name};"
            out.println ""
        }
    }
    out.println "}"
}

def calcFields(table) {
    DasUtil.getColumns(table).reduce([]) { fields, col ->
        def spec = Case.LOWER.apply(col.getDataType().getSpecification())
        def typeStr = typeMapping.find { p, t -> p.matcher(spec).find() }.value
        if (typeStr == "Long" && (Case.LOWER.apply(col.getName()).contains("_value")
                || Case.LOWER.apply(col.getName()).contains("_cost")
                || Case.LOWER.apply(col.getName()).contains("_tax"))
        ) {
            typeStr = "BigDecimal"
        }
        fields += [[
                           name   : javaName(col.getName(), false),
                           type   : typeStr,
                           annos  : "",
                           colName: col.getName(),
                           primary: DasUtil.isPrimary(col)]]
    }
}

def javaName(str, capitalize) {
    def s = com.intellij.psi.codeStyle.NameUtil.splitNameIntoWords(str)
            .collect { Case.LOWER.apply(it).capitalize() }
            .join("")
            .replaceAll(/[^\p{javaJavaIdentifierPart}[_]]/, "_")
    capitalize || s.length() == 1 ? s : Case.LOWER.apply(s[0]) + s[1..-1]
}

def generateDTO(out, className, fields) {
    def packageFullName = packageName + ".domain.dtos${packageSubName};"
    out.println "package $packageFullName"
    out.println ""
    out.println "import com.fasterxml.jackson.annotation.JsonFormat;"
    out.println "import com.fasterxml.jackson.annotation.JsonIgnoreProperties;"
    out.println "import lombok.AllArgsConstructor;"
    out.println "import lombok.NoArgsConstructor;"
    out.println "import lombok.Getter;"
    out.println "import lombok.Setter;"
    out.println "import java.util.*;"
    out.println ""
    out.println "@Setter"
    out.println "@Getter"
    out.println "@NoArgsConstructor"
    out.println "@AllArgsConstructor"
    out.println "@JsonIgnoreProperties(ignoreUnknown = true)"
    out.println "public class $className {"
    fields.each() {
        if (it.annos != "") out.println "  ${it.annos}"
        if (it.type == 'Date') {
            out.println "   @JsonFormat(shape= JsonFormat.Shape.STRING, pattern=\"dd/MM/yyyy\",timezone = \"Asia/Saigon\" )"
        }
        out.println "   private ${it.type} ${it.name};"
    }
    out.println "}"
}

def generateQModel(out, className, fields, tableName) {
    out.println "package ${packageName}.domain.mapper;\n" +
            "\n" +
            "import ${packageName}.domain.dtos.${className}DTO;\n" +
            "import ${packageName}.domain.entities.${className};\n" +
            "import org.mapstruct.Mapper;\n" +
            "import org.mapstruct.Mapping;\n" +
            "\n" +
            "import java.util.List;\n" +
            "\n" +
            "@Mapper(componentModel = \"spring\")\n" +
            "public interface ${className}Mapper {\n" +
            "    ${className}DTO toDto(${className} model);\n" +
            "    List<${className}DTO> toDtoList(List<${className}> lsModels);\n" +
            "    ${className} toEntity(${className}DTO dto);\n" +
            "    List<${className}> toEntityList(List<${className}DTO> ls${className}Dto);\n" +
            "}"
}

def generateRepo(out, className, fields, tableName) {
    def repoName = className + "Repo"
    def lowerCaseClassName = className.toString().toLowerCase()
    def modelPath = packageName + ".domain.entities${packageSubName}." + className
    def packageFullName = packageName + ".repo.jpa.${lowerCaseClassName};"
    out.println "package $packageFullName"
    out.println ""
    out.println "import $modelPath;"
    out.println "import ${packageName}.repo.dao.${lowerCaseClassName}.${className}CustomRepo;"
    out.println "import org.springframework.data.jpa.repository.JpaRepository;"
    out.println ""
    out.println "public interface $repoName extends JpaRepository< $className, Long>, ${className}CustomRepo  {"

    out.println "}"
}

def generateRepoCustom(out, className, fields, tableName) {
    def nameClass = className + ".class"
    def repoName = className + "Repo"
    def lowerCaseClassName = className.toString().toLowerCase()
    def repoCustomName = className + "CustomRepo"
    def modelPath = packageName + ".domain.entities${packageSubName}." + className
    def packageFullName = packageName + ".repo.dao.${lowerCaseClassName};"
    out.println "package $packageFullName"
    out.println ""
    out.println "public interface $repoCustomName {"
    out.println ""
    out.println "}"
}

def generateRepoImpl(out, className, fields, tableName) {
    def lowerCaseClassName = className.toString().toLowerCase()
    def packageFullName = packageName + ".repo.dao.${lowerCaseClassName};"
    out.println "package $packageFullName"
    out.println ""
    out.println "import org.apache.logging.log4j.LogManager;"
    out.println "import org.apache.logging.log4j.Logger;"
    out.println "import jakarta.persistence.EntityManager;"
    out.println "import jakarta.persistence.PersistenceContext;"
    out.println "import org.springframework.stereotype.Repository;"
    out.println ""
    out.println "@Repository"
    out.println "public class ${className}CustomRepoImpl implements ${className}CustomRepo {"
    out.println "    protected final Logger LOG = LogManager.getLogger(this.getClass());"
    out.println "    @PersistenceContext"
    out.println "    private EntityManager em;"

    out.println "}"
}

def generateService(out, className, fields, tableName) {
    def lowerCaseClassName = className.toString().toLowerCase()
    out.println "package ${packageName}.service.abs.${lowerCaseClassName};\n" +
            "\n" +
            "import ${packageName}.rest.dto.response.BaseResponseDTO;\n" +
            "import ${packageName}.domain.dtos.${className}DTO;\n" +
            "import org.springframework.data.domain.Page;\n" +
            "import org.springframework.data.domain.Pageable;\n" +
            "\n" +
            "public interface ${className}Service {\n" +
            "    Page<${className}DTO> search(${className}DTO ${tableClassObject}DTO, Pageable pageable);\n" +
            "\n" +
            "    BaseResponseDTO save${className}(${className}DTO ${tableClassObject}DTO);\n" +
            "\n" +
            "    BaseResponseDTO update${className}(${className}DTO ${tableClassObject}DTO);\n" +
            "\n" +
            "    BaseResponseDTO delete${className}(Long id);\n" +
            "\n}"
}

def generateServiceImpl(out, className, fields, tableName) {
    def lowerCaseClassName = className.toString().toLowerCase()
    out.println "package ${packageName}.service.impl.${lowerCaseClassName};\n\n" +
            "import ${packageName}.domain.mapper.${className}Mapper;\n" +
            "import ${packageName}.rest.dto.response.BaseResponseDTO;\n" +
            "import ${packageName}.domain.dtos.${className}DTO;\n" +
            "import ${packageName}.domain.entities.${className};\n" +
            "import ${packageName}.repo.jpa.${lowerCaseClassName}.${className}Repo;\n" +
            "import ${packageName}.service.abs.${lowerCaseClassName}.${className}Service;\n" +
            "import org.slf4j.Logger;\n" +
            "import org.slf4j.LoggerFactory;\n" +
            "import org.springframework.data.domain.Page;\n" +
            "import org.springframework.data.domain.Pageable;\n" +
            "import org.springframework.data.domain.PageImpl;\n" +
            "import org.springframework.stereotype.Service;\n" +
            "import org.springframework.transaction.annotation.Transactional;\n" +
            "import vn.nodo.c09.tangthu.exception.BusinessException;\n" +
            "import vn.nodo.c09.tangthu.common.Constants;\n" +
            "\n" +
            "@Service\n" +
            "public class ${className}ServiceImpl implements ${className}Service {\n" +
            "    private static final Logger logger = LoggerFactory.getLogger(${className}ServiceImpl.class);\n" +
            "    private final ${className}Mapper mapper;\n" +
            "    private final ${className}Repo repo;\n" +
            "\n" +
            "    public ${className}ServiceImpl(${className}Mapper mapper, ${className}Repo repo" +
            ") {\n" +
            "        this.repo = repo;\n" +
            "        this.mapper = mapper;\n" +
            "    }\n" +
            "\n" +
            "    @Override\n" +
            "    @Transactional(rollbackFor = Throwable.class)\n" +
            "    public BaseResponseDTO save${className}(${className}DTO request) {\n" +
            "        try {\n" +
            "            ${className} entity = mapper.toEntity(request);\n" +
            "            entity.setStatus(Constants.STATUS.ACTIVE);\n" +
            "            entity.setDeleted(Constants.DELETE.INACTIVE);\n" +
            "            repo.save(entity);\n" +
            "            return new BaseResponseDTO();\n" +
            "        } catch (Exception e) {\n" +
            "            logger.error(e.getMessage(), e);\n" +
            "            throw e;\n" +
            "        }\n" +
            "    }\n" +
            "\n" +
            "    @Override\n" +
            "    @Transactional(rollbackFor = Throwable.class)\n" +
            "    public BaseResponseDTO update${className}(${className}DTO request) {\n" +
            "        try {\n" +
            "            if (request.getId() == null) throw new BusinessException(\"Id is required\");\n" +
            "            ${className} domain = repo.findById(request.getId())\n" +
            "                    .orElseThrow(() -> new BusinessException(\"Could not find id \" + request.getId()));\n" +
            "            ${className} entity = mapper.toEntity(request);\n" +
            "            entity.setCreatedBy(domain.getCreatedBy());\n" +
            "            entity.setCreatedDate(domain.getCreatedDate());\n" +
            "            repo.save(entity);\n" +
            "            return new BaseResponseDTO();\n" +
            "        } catch (Exception e) {\n" +
            "            logger.error(e.getMessage(), e);\n" +
            "            throw e;\n" +
            "        }\n" +
            "    }\n" +
            "\n" +
            "    @Override\n" +
            "    @Transactional(rollbackFor = Throwable.class)\n" +
            "    public BaseResponseDTO delete${className}(Long id) {\n" +
            "        try {\n" +
            "            ${className} entity = repo.findById(id).orElseThrow(() -> new BusinessException(\"Could not find id \" + id));\n" +
            "            entity.setDeleted(Constants.DELETE.ACTIVE);\n" +
            "            return new BaseResponseDTO();\n" +
            "        } catch (Exception e) {\n" +
            "            logger.error(e.getMessage(), e);\n" +
            "            throw e;\n" +
            "        }\n" +
            "    }\n" +
            "\n" +
            "    @Override\n" +
            "    public Page<${className}DTO> search(${className}DTO request, Pageable pageable) {\n" +
            "        try {\n" +
            "            Page<${className}> rs = repo.findAll(pageable);\n" +
            "            return new PageImpl<>(mapper.toDtoList(rs.getContent()), pageable, rs.getTotalElements());\n" +
            "        } catch (Exception e) {\n" +
            "            logger.error(e.getMessage(), e);\n" +
            "            throw e;\n" +
            "        }\n" +
            "    }\n" +
            "}"
}


def generateController(out, className, fields, tableName) {
    def lowerCaseClassName = className.toString().toLowerCase()
    out.println "package ${packageName}.rest.clients.${lowerCaseClassName};\n" +
            "\n" +
            "import ${packageName}.service.abs.${lowerCaseClassName}.${className}Service;\n" +
            "import ${packageName}.domain.dtos.${className}DTO;\n" +
            "import org.slf4j.Logger;\n" +
            "import org.slf4j.LoggerFactory;\n" +
            "import org.springframework.data.domain.Pageable;\n" +
            "import org.springframework.data.web.PageableDefault;\n" +
            "import org.springframework.web.bind.annotation.*;\n" +
            "import vn.nodo.c09.tangthu.common.Constants;\n" +
            "import vn.nodo.c09.tangthu.common.ResponseFactory;\n" +
            "import org.springframework.http.ResponseEntity;\n" +
            "import jakarta.validation.Valid;\n" +
            "\n" +
            "@RestController\n" +
            "@RequestMapping(Constants.CLIENT_API)\n" +
            "public class ${className}Controller {\n" +
            "    private static final Logger logger = LoggerFactory.getLogger(${className}Controller.class);\n" +
            "\n" +
            "    private final ${className}Service ${tableClassObject}Service;\n" +
            "    final ResponseFactory responseFactory;\n" +
            "\n" +
            "    public ${className}Controller(${className}Service ${tableClassObject}Service, ResponseFactory responseFactory) {\n" +
            "        this.${tableClassObject}Service = ${tableClassObject}Service;\n" +
            "        this.responseFactory = responseFactory;\n" +
            "    }\n" +
            "\n" +
            "    @PostMapping(value = \"/search\")   \n" +
            "    public ResponseEntity<?> search(${className}DTO request, @PageableDefault Pageable pageable) {\n" +
            "       return responseFactory.success(${tableClassObject}Service.search(request, pageable));\n" +
            "    }\n" +
            "    \n" +
            "    @PostMapping(value = \"/add\")\n" +
            "    public ResponseEntity<?> add(@RequestBody @Valid ${className}DTO request) {\n" +
            "        return responseFactory.success(${tableClassObject}Service.save${className}(request));\n" +
            "    }\n" +
            "    \n" +
            "    @PostMapping(value = \"/update\")\n" +
            "    public ResponseEntity<?> update(@RequestBody @Valid ${className}DTO request) {\n" +
            "        return responseFactory.success(${tableClassObject}Service.update${className}(request));\n" +
            "    }\n" +
            "    \n" +
            "    @DeleteMapping(value = \"/delete/{id}\")\n" +
            "    public ResponseEntity<?> delete(@PathVariable(\"id\") Long id) {\n" +
            "        return responseFactory.success(${tableClassObject}Service.delete${className}(id));\n" +
            "    }\n" +
            "}"
}
