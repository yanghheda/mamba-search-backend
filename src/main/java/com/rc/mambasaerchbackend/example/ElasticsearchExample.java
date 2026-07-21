package com.rc.mambasaerchbackend.example;

import com.rc.mambasaerchbackend.common.BaseResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

/**
 * Elasticsearch 使用示例（仅在配置了 spring.elasticsearch.uris 时加载）
 */
@Tag(name = "示例-Elasticsearch", description = "Elasticsearch 操作示例接口")
@RestController
@RequestMapping("/example/es")
@ConditionalOnProperty(prefix = "spring.elasticsearch", name = "uris")
public class ElasticsearchExample {

    private final EsDocRepository repository;

    public ElasticsearchExample(EsDocRepository repository) {
        this.repository = repository;
    }

    @Operation(summary = "创建/更新文档")
    @PostMapping("/save")
    public BaseResponse<EsDocEntity> save(@RequestBody EsDocEntity doc) {
        return BaseResponse.success(repository.save(doc));
    }

    @Operation(summary = "根据ID查询文档")
    @GetMapping("/get")
    public BaseResponse<EsDocEntity> get(@RequestParam String id) {
        Optional<EsDocEntity> doc = repository.findById(id);
        return doc.map(BaseResponse::success)
                .orElse(BaseResponse.success(null));
    }

    @Operation(summary = "根据ID删除文档")
    @DeleteMapping("/delete")
    public BaseResponse<Void> delete(@RequestParam String id) {
        repository.deleteById(id);
        return BaseResponse.success();
    }

    @Operation(summary = "查询全部文档")
    @GetMapping("/all")
    public BaseResponse<Iterable<EsDocEntity>> all() {
        return BaseResponse.success(repository.findAll());
    }
}
