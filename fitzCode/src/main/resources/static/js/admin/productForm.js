$(document).ready(function() {
    // 상위 카테고리 변경 시 하위 카테고리 및 사이즈 테이블
    $('#parentCategoryId').change(function() {
        let parentId = $(this).val();
        let $sizeTableBody = $('#sizeTable tbody');
        $sizeTableBody.empty();

        if (parentId) {
            $.get('/admin/products/categories/child', { parentId: parentId }, function(data) {
                let $childCategory = $('#categoryId');
                $childCategory.empty().append('<option value="">하위 카테고리 선택</option>');
                $.each(data, function(index, category) {
                    $childCategory.append('<option value="' + category.categoryId + '">' + category.name + '</option>');
                });
                $childCategory.prop('disabled', false);
            });

            $.get('/admin/products/sizes', { parentId: parentId }, function(sizes) {
                $.each(sizes, function(index, size) {
                    $sizeTableBody.append(
                        '<tr>' +
                        '<td>' + size.description + '</td>' +
                        '<td>' +
                        '<input type="hidden" name="productSizes[' + index + '].sizeCode" value="' + size.code + '" />' +
                        '<input type="number" name="productSizes[' + index + '].stock" min="0" value="0" />' +
                        '</td>' +
                        '</tr>'
                    );
                });
            });
        } else {
            $('#categoryId').empty().append('<option value="">하위 카테고리 선택</option>').prop('disabled', true);
        }
    });

    // 메인 이미지 미리보기
    $('#mainImageFile').change(function() {
        let file = this.files[0];
        if (file) {
            let reader = new FileReader();
            reader.onload = function(e) {
                $('#mainImagePreview').html('<img src="' + e.target.result + '" alt="Main Image Preview" />');
            };
            reader.readAsDataURL(file);
        }
    });

    // 추가 이미지 추가 및 미리보기
    let imageIndex = 1;
    $('#addImage').click(function() {
        let newInput = $(
            '<div class="additional-image">' +
            '<input type="file" name="additionalImageFiles" accept="image/*" />' +
            '<button type="button" class="remove-btn">삭제</button>' +
            '</div>'
        );
        $('#additionalImages').append(newInput);
        newInput.find('input').change(function() {
            let file = this.files[0];
            if (file) {
                let reader = new FileReader();
                reader.onload = function(e) {
                    $('#additionalImagePreview').append('<img src="' + e.target.result + '" alt="Additional Image Preview" />');
                };
                reader.readAsDataURL(file);
            }
        });
        imageIndex++;
    });

    $(document).on('click', '.remove-btn', function() {
        let $parent = $(this).parent();
        let $img = $('#additionalImagePreview img').eq($parent.index());
        $img.remove();
        $parent.remove();
    });

    // 상태 버튼 선택
    $('.status-btn').click(function() {
        $('.status-btn').removeClass('active');
        $(this).addClass('active');
        $('#status').val($(this).data('value'));
    });
});