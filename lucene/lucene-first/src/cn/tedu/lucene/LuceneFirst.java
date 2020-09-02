package cn.tedu.lucene;

import org.apache.commons.io.FileUtils;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.TextField;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import org.apache.lucene.store.RAMDirectory;
import org.junit.Test;

import java.io.File;
import java.io.IOException;

public class LuceneFirst {
    @Test

    public static void main(String[] args) throws IOException {
//        1、创建一个Director对象，指定索引库保存的位置。
        //把索引索引库保存在内存中
        //Directory directory = new RAMDirectory();
        //把索引库保存在磁盘
        FSDirectory directory = FSDirectory.open(new File("E:\\Desktop\\AV\\12-lucene\\index").toPath());
//        2、基于Directory对象创建一个IndexWriter对象
        IndexWriter indexWriter = new IndexWriter(directory,new IndexWriterConfig());
//        3、读取磁盘上的文件，对应每个文件创建一个文档对象。
        File dir = new File("E:\\Desktop\\AV\\12-lucene\\02.参考资料\\searchsource");
        File[] files = dir.listFiles();
        for (File file:files){
            //取文件名
            String name = file.getName();
            //取文件路径
            String path = file.getPath();
            //文件的内容
            String fileContent = FileUtils.readFileToString(file, "utf-8");
            //文件大小
            long fileSize = FileUtils.sizeOf(file);
            //创建Field
            //参数1：域的名称，参数2：域的内容，参数3：是否存储
            TextField fieldName = new TextField("name", name, Field.Store.YES);
            TextField fieldPath = new TextField("path", path, Field.Store.YES);
            TextField fieldContent = new TextField("content", fileContent, Field.Store.YES);
            TextField fieldSize = new TextField("size", fileSize+"", Field.Store.YES);
            //创建文档对象
            Document document = new Document();
            //4.向文档对象中添加域
            document.add(fieldName);
            document.add(fieldPath);
            document.add(fieldContent);
            document.add(fieldSize);
            //        5、把文档对象写入索引库
            indexWriter.addDocument(document);
        }
        indexWriter.close();
//        6、关闭indexwriter对象
    }
}
